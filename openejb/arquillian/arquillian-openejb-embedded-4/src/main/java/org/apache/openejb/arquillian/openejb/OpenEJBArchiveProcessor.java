package org.apache.openejb.arquillian.openejb;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.openejb.OpenEJBException;
import org.apache.openejb.OpenEJBRuntimeException;
import org.apache.openejb.config.AppModule;
import org.apache.openejb.config.EjbModule;
import org.apache.openejb.config.ReadDescriptors;
import org.apache.openejb.jee.EjbJar;
import org.apache.openejb.jee.ManagedBean;
import org.apache.openejb.jee.TransactionType;
import org.apache.openejb.jee.oejb3.EjbDeployment;
import org.apache.openejb.jee.oejb3.OpenejbJar;
import org.apache.openejb.loader.IO;
import org.apache.xbean.finder.AnnotationFinder;
import org.apache.xbean.finder.archive.ClassesArchive;
import org.jboss.arquillian.container.spi.context.annotation.DeploymentScoped;
import org.jboss.arquillian.container.test.spi.client.deployment.ApplicationArchiveProcessor;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.arquillian.test.spi.annotation.SuiteScoped;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.asset.UrlAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.base.filter.IncludeRegExpPaths;

public class OpenEJBArchiveProcessor implements ApplicationArchiveProcessor {
    private static final Logger LOGGER = Logger.getLogger(OpenEJBArchiveProcessor.class.getName());

    private static final String META_INF = "META-INF/";
    private static final String WEB_INF = "WEB-INF/";
    private static final String EJB_JAR_XML = "ejb-jar.xml";

    private static final String BEANS_XML = "beans.xml";
    private static final String VALIDATION_XML = "validation.xml";
    private static final String PERSISTENCE_XML = "persistence.xml";
    private static final String OPENEJB_JAR_XML = "openejb-jar.xml";
    private static final String ENV_ENTRIES_PROPERTIES = "env-entries.properties";

    @Inject
    @SuiteScoped
    private InstanceProducer<AppModule> module;

    @Override
    public void process(final Archive<?> archive, final TestClass testClass) {
        final Class<?> javaClass = testClass.getJavaClass();
        final AppModule appModule = new AppModule(javaClass.getClassLoader(), javaClass.getName());

        final String prefix;
        if (archive instanceof WebArchive) {
            prefix = WEB_INF;
        } else {
            prefix = META_INF;
        }

        // add the test as a managed bean to be able to inject into it easily
        {
            final EjbJar ejbJar = new EjbJar();
            final OpenejbJar openejbJar = new OpenejbJar();
            final ManagedBean bean = ejbJar.addEnterpriseBean(new ManagedBean(javaClass.getSimpleName(), javaClass.getName(), true));
            bean.setTransactionType(TransactionType.BEAN);
            final EjbDeployment ejbDeployment = openejbJar.addEjbDeployment(bean);
            ejbDeployment.setDeploymentId(javaClass.getName());
            appModule.getEjbModules().add(new EjbModule(ejbJar, openejbJar));
        }

        final org.apache.xbean.finder.archive.Archive finderArchive = finderArchive(archive, appModule.getClassLoader());

        final EjbJar ejbJar;
        final Node ejbJarXml = archive.get(prefix.concat(EJB_JAR_XML));
        if (ejbJarXml != null) {
            try {
                ejbJar = ReadDescriptors.readEjbJar(ejbJarXml.getAsset().openStream());
            } catch (OpenEJBException e) {
                throw new OpenEJBRuntimeException(e);
            }
        } else {
            ejbJar = new EjbJar();
        }

        final EjbModule ejbModule = new EjbModule(ejbJar);
        ejbModule.setFinder(new AnnotationFinder(finderArchive));
        appModule.getEjbModules().add(ejbModule);

        {
            final Node beansXml = archive.get(prefix.concat(BEANS_XML));
            if (beansXml != null) {
                ejbModule.getAltDDs().put(BEANS_XML, new AssetSource(beansXml.getAsset()));
            }
        }

        {
            final Node persistenceXml = archive.get(prefix.concat(PERSISTENCE_XML));
            if (persistenceXml != null) {
                final Asset asset = persistenceXml.getAsset();
                if (asset instanceof UrlAsset) {
                    appModule.getAltDDs().put(PERSISTENCE_XML, Arrays.asList(get(URL.class, "url", asset)));
                } else if (asset instanceof FileAsset) {
                    try {
                        appModule.getAltDDs().put(PERSISTENCE_XML, Arrays.asList(get(File.class, "file", asset).toURI().toURL()));
                    } catch (MalformedURLException e) {
                        appModule.getAltDDs().put(PERSISTENCE_XML, Arrays.asList(new AssetSource(persistenceXml.getAsset())));
                    }
                } else {
                    appModule.getAltDDs().put(PERSISTENCE_XML, Arrays.asList(new AssetSource(persistenceXml.getAsset())));
                }
            }
        }

        {
            final Node openejbJarXml = archive.get(prefix.concat(OPENEJB_JAR_XML));
            if (openejbJarXml != null) {
                ejbModule.getAltDDs().put(OPENEJB_JAR_XML, new AssetSource(openejbJarXml.getAsset()));
            }
        }

        {
            final Node validationXml = archive.get(prefix.concat(VALIDATION_XML));
            if (validationXml != null) {
                ejbModule.getAltDDs().put(VALIDATION_XML, new AssetSource(validationXml.getAsset()));
            }
        }

        {
            final Node envEntriesProperties = archive.get(prefix.concat(ENV_ENTRIES_PROPERTIES));
            if (envEntriesProperties != null) {
                InputStream is = null;
                final Properties properties = new Properties();
                try {
                    is = envEntriesProperties.getAsset().openStream();
                    properties.load(is);
                    ejbModule.getAltDDs().put(ENV_ENTRIES_PROPERTIES, properties);

                    // do it for test class too
                    appModule.getEjbModules().iterator().next().getAltDDs().put(ENV_ENTRIES_PROPERTIES, properties);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "can't read env-entries.properties", e);
                } finally {
                    IO.close(is);
                }
            }
        }

        // export it to be usable in the container
        module.set(appModule);
    }

    private <T> T get(Class<T> fileClass, String attr, Asset asset) {
        try {
            final Field field = asset.getClass().getDeclaredField(attr);
            field.setAccessible(true);
            return fileClass.cast(field.get(asset));
        } catch (Exception e) {
            return null;
        }
    }

    private org.apache.xbean.finder.archive.Archive finderArchive(final Archive<?> archive, final ClassLoader cl) {
        final List<Class<?>> classes = new ArrayList<Class<?>>();
        final Map<ArchivePath, Node> content = archive.getContent(new IncludeRegExpPaths(".*.class"));
        for (Map.Entry<ArchivePath, Node> node : content.entrySet()) {
            final String classname = name(node.getKey().get());
            try {
                classes.add(cl.loadClass(classname));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return new ClassesArchive(classes);
    }

    private static String name(final String raw) {
        final String name = raw.replace('/', '.');
        return name.substring(1, name.length() - 6);
    }

    private static class AssetSource implements ReadDescriptors.Source {
        private Asset asset;

        private AssetSource(Asset asset) {
            this.asset = asset;
        }

        @Override
        public InputStream get() throws IOException {
            return asset.openStream();
        }
    }
}
