/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.openejb.config;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.openejb.jee.CmpField;
import org.apache.openejb.jee.EjbJar;
import org.apache.openejb.jee.EnterpriseBean;
import org.apache.openejb.jee.EntityBean;
import org.apache.openejb.jee.PersistenceType;
import org.apache.openejb.jee.jpa.Attributes;
import org.apache.openejb.jee.jpa.Basic;
import org.apache.openejb.jee.jpa.Column;
import org.apache.openejb.jee.jpa.Entity;
import org.apache.openejb.jee.jpa.EntityMappings;
import org.apache.openejb.jee.jpa.Field;
import org.apache.openejb.jee.jpa.Id;
import org.apache.openejb.jee.jpa.JoinColumn;
import org.apache.openejb.jee.jpa.JoinTable;
import org.apache.openejb.jee.jpa.ManyToOne;
import org.apache.openejb.jee.jpa.NamedQuery;
import org.apache.openejb.jee.jpa.OneToMany;
import org.apache.openejb.jee.jpa.OneToOne;
import org.apache.openejb.jee.jpa.PrimaryKeyJoinColumn;
import org.apache.openejb.jee.jpa.RelationField;
import org.apache.openejb.jee.jpa.SecondaryTable;
import org.apache.openejb.jee.jpa.Table;
import org.apache.openejb.jee.jpa.AttributeOverride;
import org.apache.openejb.jee.oejb3.OpenejbJar;
import org.apache.openejb.jee.sun.Cmp;
import org.apache.openejb.jee.sun.CmpFieldMapping;
import org.apache.openejb.jee.sun.CmrFieldMapping;
import org.apache.openejb.jee.sun.ColumnName;
import org.apache.openejb.jee.sun.ColumnPair;
import org.apache.openejb.jee.sun.Ejb;
import org.apache.openejb.jee.sun.EntityMapping;
import org.apache.openejb.jee.sun.Finder;
import org.apache.openejb.jee.sun.JaxbSun;
import org.apache.openejb.jee.sun.OneOneFinders;
import org.apache.openejb.jee.sun.SunCmpMapping;
import org.apache.openejb.jee.sun.SunCmpMappings;
import org.apache.openejb.jee.sun.SunEjbJar;

//
// Note to developer:  the best doc on what the sun-cmp-mappings element mean can be foudn here
//   http://java.sun.com/j2ee/1.4/docs/devguide/dgcmp.html
//   https://glassfish.dev.java.net/javaee5/docs/DG/beajj.html
//

public class SunConversion implements DynamicDeployer {
    public AppModule deploy(AppModule appModule) {
        for (EjbModule ejbModule : appModule.getEjbModules()) {
            convertModule(ejbModule, appModule.getCmpMappings());
        }
        return appModule;
    }

    private SunEjbJar getSunEjbJar(EjbModule ejbModule) {
        Object altDD = ejbModule.getAltDDs().get("sun-ejb-jar.xml");
        if (altDD instanceof String) {
            try {
                altDD = JaxbSun.unmarshal(SunCmpMappings.class, new ByteArrayInputStream(((String)altDD).getBytes()));
            } catch (Exception e) {
                // todo warn about not being able to parse sun descriptor
            }
        }
        if (altDD instanceof URL) {
            try {
                altDD = JaxbSun.unmarshal(SunEjbJar.class, ((URL)altDD).openStream());
            } catch (Exception e) {
                // todo warn about not being able to parse sun descriptor
            }
        }
        if (altDD instanceof SunEjbJar) {
            return (SunEjbJar) altDD;
        }
        return null;
    }

    private SunCmpMappings getSunCmpMappings(EjbModule ejbModule) {
        Object altDD = ejbModule.getAltDDs().get("sun-cmp-mappings.xml");
        if (altDD instanceof String) {
            try {
                altDD = JaxbSun.unmarshal(SunCmpMappings.class, new ByteArrayInputStream(((String)altDD).getBytes()));
            } catch (Exception e) {
                // todo warn about not being able to parse sun descriptor
            }
        }
        if (altDD instanceof URL) {
            try {
                altDD = JaxbSun.unmarshal(SunCmpMappings.class, ((URL)altDD).openStream());
            } catch (Exception e) {
                e.printStackTrace();
                // todo warn about not being able to parse sun descriptor
            }
        }
        if (altDD instanceof SunCmpMappings) {
            return (SunCmpMappings) altDD;
        }
        return null;
    }

    public void convertModule(EjbModule ejbModule, EntityMappings entityMappings) {
       Map<String, EntityData> entities =  new TreeMap<String, EntityData>();
        for (Entity entity : entityMappings.getEntity()) {
            entities.put(entity.getDescription(), new SunConversion.EntityData(entity));
        }


        // merge data from sun-ejb-jar.xml file
        mergeEntityMappings(entities, ejbModule.getEjbJar(), ejbModule.getOpenejbJar(), getSunEjbJar(ejbModule));

        // merge data from sun-cmp-mappings.xml file
        SunCmpMappings sunCmpMappings = getSunCmpMappings(ejbModule);
        if (sunCmpMappings != null) {
            for (SunCmpMapping sunCmpMapping : sunCmpMappings.getSunCmpMapping()) {
                mergeEntityMappings(entities, ejbModule, entityMappings, sunCmpMapping);
            }
        }
    }

    private void mergeEntityMappings(Map<String, EntityData> entities, EjbJar ejbJar, OpenejbJar openejbJar, SunEjbJar sunEjbJar) {
        if (openejbJar == null) return;
        if (sunEjbJar == null) return;
        if (sunEjbJar.getEnterpriseBeans() == null) return;

        for (Ejb ejb : sunEjbJar.getEnterpriseBeans().getEjb()) {
            Cmp cmp = ejb.getCmp();
            if (cmp == null) {
                // skip non cmp beans
                continue;
            }

            // skip all non-CMP beans
            EnterpriseBean enterpriseBean = ejbJar.getEnterpriseBean(ejb.getEjbName());
            if (!(enterpriseBean instanceof org.apache.openejb.jee.EntityBean) ||
                    ((EntityBean) enterpriseBean).getPersistenceType() != PersistenceType.CONTAINER) {
                continue;
            }
            EntityBean bean = (EntityBean) enterpriseBean;
            EntityData entityData = entities.get(ejb.getEjbName());
            if (entityData == null) {
                // todo warn no such ejb in the ejb-jar.xml
                continue;
            }

            Collection<String> cmpFields = new ArrayList<String>(bean.getCmpField().size());
            for (CmpField cmpField : bean.getCmpField()) {
                cmpFields.add(cmpField.getFieldName());
            }

            OneOneFinders oneOneFinders = cmp.getOneOneFinders();
            if (oneOneFinders != null) {
                for (Finder finder : oneOneFinders.getFinder()) {
                    List<List<String>> params = parseQueryParamters(finder.getQueryParams());
                    String queryFilter = finder.getQueryFilter();
                    String ejbQl = convertToEjbQl(entityData.entity.getName(), cmpFields, finder.getQueryParams(), queryFilter);

                    NamedQuery namedQuery = new NamedQuery();

                    StringBuilder name = new StringBuilder();
                    name.append(entityData.entity.getName()).append(".").append(finder.getMethodName());
                    if (!params.isEmpty()) {
                        name.append('(');
                        boolean first = true;
                        for (List<String> methodParam : params) {
                            if (!first) name.append(",");
                            name.append(methodParam.get(0));
                            first = false;
                        }
                        name.append(')');
                    }
                    namedQuery.setName(name.toString());
                    namedQuery.setQuery(ejbQl);
                    entityData.entity.getNamedQuery().add(namedQuery);
                }
            }
        }
    }

    public void mergeEntityMappings(Map<String, EntityData> entities, EjbModule ejbModule, EntityMappings entityMappings, SunCmpMapping sunCmpMapping) {
        for (EntityMapping bean : sunCmpMapping.getEntityMapping()) {
            SunConversion.EntityData entityData = entities.get(bean.getEjbName());
            if (entityData == null) {
                // todo warn no such ejb in the ejb-jar.xml
                continue;
            }

            Table table = new Table();
            // table.setSchema(schema);
            table.setName(bean.getTableName());
            entityData.entity.setTable(table);

            // warn about no equivalent of the consistence modes in sun file

            for (org.apache.openejb.jee.sun.SecondaryTable sunSecondaryTable : bean.getSecondaryTable()) {
                SecondaryTable secondaryTable = new SecondaryTable();
                secondaryTable.setName(sunSecondaryTable.getTableName());
                for (ColumnPair columnPair : sunSecondaryTable.getColumnPair()) {
                    SunColumnName localColumnName = new SunColumnName(columnPair.getColumnName().get(0));
                    SunColumnName referencedColumnName = new SunColumnName(columnPair.getColumnName().get(1));

                    // if user specified in reverse order, swap
                    if (localColumnName.table != null) {
                        SunColumnName temp = localColumnName;
                        localColumnName = referencedColumnName;
                        referencedColumnName = temp;
                    }

                    PrimaryKeyJoinColumn primaryKeyJoinColumn = new PrimaryKeyJoinColumn();
                    primaryKeyJoinColumn.setName(localColumnName.column);
                    primaryKeyJoinColumn.setReferencedColumnName(referencedColumnName.column);
                    secondaryTable.getPrimaryKeyJoinColumn().add(primaryKeyJoinColumn);
                }
            }

            for (CmpFieldMapping cmpFieldMapping : bean.getCmpFieldMapping()) {
                String fieldName = cmpFieldMapping.getFieldName();
                Field field = entityData.fields.get(fieldName);

                if (field == null) {
                    // todo warn no such cmp-field in the ejb-jar.xml
                    continue;
                }

                boolean readOnly = cmpFieldMapping.getReadOnly() != null;

                for (ColumnName columnName : cmpFieldMapping.getColumnName()) {
                    SunColumnName sunColumnName = new SunColumnName(columnName);
                    Column column = new Column();
                    column.setTable(sunColumnName.table);
                    column.setName(sunColumnName.column);
                    if (readOnly) {
                        column.setInsertable(false);
                        column.setUpdatable(false);
                    }
                    field.setColumn(column);
                }
                // todo set fetch lazy when fetchWith is null
                // FetchedWith fetchedWith = cmpFieldMapping.getFetchedWith();
            }

            for (CmrFieldMapping cmrFieldMapping : bean.getCmrFieldMapping()) {
                String fieldName = cmrFieldMapping.getCmrFieldName();
                cmrFieldMapping.getColumnPair();
                RelationField field = entityData.relations.get(fieldName);
                if (field == null) {
                    // todo warn no such cmr-field in the ejb-jar.xml
                    continue;
                }

                if (field instanceof OneToOne) {
                    for (ColumnPair columnPair : cmrFieldMapping.getColumnPair()) {
                        SunColumnName localColumnName = new SunColumnName(columnPair.getColumnName().get(0));
                        SunColumnName referencedColumnName = new SunColumnName(columnPair.getColumnName().get(1));

                        // if user specified in reverse order, swap
                        if (localColumnName.table != null) {
                            SunColumnName temp = localColumnName;
                            localColumnName = referencedColumnName;
                            referencedColumnName = temp;
                        }

                        boolean isFk = !entityData.hasPkColumnMapping(localColumnName.column);
                        if (isFk) {
                            // Make sure that the field with the FK is marked as the owning field
                            field.setMappedBy(null);
                            if (field.getRelatedField() != null) {
                                field.getRelatedField().setMappedBy(field.getName());
                            }

                            JoinColumn joinColumn = new JoinColumn();
                            joinColumn.setName(localColumnName.column);
                            joinColumn.setReferencedColumnName(referencedColumnName.column);
                            field.getJoinColumn().add(joinColumn);
                        } else {
                            // Make sure that the field with the FK is marked as the owning field
                            if (field.getRelatedField() != null) {
                                field.getRelatedField().setMappedBy(null);
                                // fk declaration will be on related field
                                continue;
                            }

                            JoinColumn joinColumn = new JoinColumn();
                            // for reverse-mapping the join column name is the other (fk) column
                            joinColumn.setName(referencedColumnName.column);
                            // and the referenced column is the local (pk) column
                            joinColumn.setReferencedColumnName(localColumnName.column);
                            field.getJoinColumn().add(joinColumn);
                        }
                    }
                } else if (field instanceof OneToMany) {
                    // Bi-directional OneToMany do not have field mappings
                    if (field.getRelatedField() != null) {
                        continue;
                    }

                    for (ColumnPair columnPair : cmrFieldMapping.getColumnPair()) {
                        SunColumnName localColumnName = new SunColumnName(columnPair.getColumnName().get(0));
                        SunColumnName otherColumnName = new SunColumnName(columnPair.getColumnName().get(1));

                        // if user specified in reverse order, swap
                        if (localColumnName.table != null) {
                            SunColumnName temp = localColumnName;
                            localColumnName = otherColumnName;
                            otherColumnName = temp;
                        }

                        JoinColumn joinColumn = new JoinColumn();
                        // for OneToMany the join column name is the other (fk) column
                        joinColumn.setName(otherColumnName.column);
                        // and the referenced column is the local (pk) column
                        joinColumn.setReferencedColumnName(localColumnName.column);
                        field.getJoinColumn().add(joinColumn);
                    }
                } else if (field instanceof ManyToOne) {
                    for (ColumnPair columnPair : cmrFieldMapping.getColumnPair()) {
                        SunColumnName localColumnName = new SunColumnName(columnPair.getColumnName().get(0));
                        SunColumnName referencedColumnName = new SunColumnName(columnPair.getColumnName().get(1));

                        // if user specified in reverse order, swap
                        if (localColumnName.table != null) {
                            SunColumnName temp = localColumnName;
                            localColumnName = referencedColumnName;
                            referencedColumnName = temp;
                        }

                        JoinColumn joinColumn = new JoinColumn();
                        joinColumn.setName(localColumnName.column);
                        joinColumn.setReferencedColumnName(referencedColumnName.column);
                        field.getJoinColumn().add(joinColumn);
                    }
                } else {
                    // skip the non owning side
                    if (field.getMappedBy() != null) continue;

                    JoinTable joinTable = new JoinTable();
                    field.setJoinTable(joinTable);
                    for (ColumnPair columnPair : cmrFieldMapping.getColumnPair()) {
                        SunColumnName localColumnName = new SunColumnName(columnPair.getColumnName().get(0));
                        SunColumnName joinTableColumnName = new SunColumnName(columnPair.getColumnName().get(1));

                        if (localColumnName.table == null || joinTableColumnName.table == null) {
                            // if user specified in reverse order, swap
                            if (localColumnName.table != null) {
                                SunColumnName temp = localColumnName;
                                localColumnName = joinTableColumnName;
                                joinTableColumnName = temp;
                            }

                            // join table is the table name of the referenced column
                            joinTable.setName(joinTableColumnName.table);

                            JoinColumn joinColumn = new JoinColumn();
                            joinColumn.setName(joinTableColumnName.column);
                            joinColumn.setReferencedColumnName(localColumnName.column);
                            joinTable.getJoinColumn().add(joinColumn);
                        } else {
                            // if user specified in reverse order, swap
                            if (localColumnName.table.equals(joinTable.getName())) {
                                SunColumnName temp = localColumnName;
                                localColumnName = joinTableColumnName;
                                joinTableColumnName = temp;
                            }

                            JoinColumn joinColumn = new JoinColumn();
                            joinColumn.setName(joinTableColumnName.column);
                            joinColumn.setReferencedColumnName(localColumnName.column);
                            joinTable.getInverseJoinColumn().add(joinColumn);
                        }

                    }
                }
            }
        }
    }

    public String convertToEjbQl(String abstractSchemaName, String queryParams, String queryFilter) {
        return convertToEjbQl(abstractSchemaName, Collections.<String>emptyList(), queryParams, queryFilter);
    }

    public String convertToEjbQl(String abstractSchemaName, Collection<String>  cmpFields, String queryParams, String queryFilter) {
        List<List<String>> variableNames = parseQueryParamters(queryParams);

        StringBuilder ejbQl = new StringBuilder();
        ejbQl.append("SELECT OBJECT(o) FROM ").append(abstractSchemaName).append(" AS o");
        String filter = convertToEjbQlFilter(cmpFields, variableNames, queryFilter);
        if (filter != null) {
            ejbQl.append(" WHERE ").append(filter);
        }
        return ejbQl.toString();
    }

    private List<List<String>> parseQueryParamters(String queryParams) {
        if (queryParams == null) return Collections.emptyList();

        List bits = Collections.list(new StringTokenizer(queryParams, " \t\n\r\f,", false));
        List<List<String>> params = new ArrayList<List<String>>(bits.size() / 2);
        for (int i = 0; i < bits.size(); i++) {
            String type = resolveType((String) bits.get(i));
            String param = (String) bits.get(++i);
            params.add(Arrays.asList(type, param));
        }
        return params;
    }

    private String resolveType(String type) {
        try {
            ClassLoader.getSystemClassLoader().loadClass(type);
            return type;
        } catch (ClassNotFoundException e) {
        }
        try {
            String javaLangType = "java.lang" + type;
            ClassLoader.getSystemClassLoader().loadClass(javaLangType);
            return javaLangType;
        } catch (ClassNotFoundException e) {
        }
        return type;
    }

    private String convertToEjbQlFilter(Collection<String> cmpFields, List<List<String>> queryParams, String queryFilter) {
        if (queryFilter == null) return null;

        Map<String, String> variableMap = new TreeMap<String, String>();
        for (String cmpField : cmpFields) {
            variableMap.put(cmpField, "o." + cmpField);
        }
        for (int i = 0; i < queryParams.size(); i++) {
            List<String> param = queryParams.get(i);
            variableMap.put(param.get(1), "?" + (i + 1));
        }

        Map<String, String> symbolMap = new TreeMap<String, String>();
        symbolMap.put("&&", "and");
        symbolMap.put("||", "or");
        symbolMap.put("!", "not");
        symbolMap.put("==", "=");
        symbolMap.put("!=", "<>");

        StringBuilder ejbQlFilter = new StringBuilder(queryFilter.length() * 2);
        List<String> tokens = tokenize(queryFilter);
        for (String token : tokens) {
            String mappedToken = symbolMap.get(token);
            if (mappedToken == null) {
                mappedToken = variableMap.get(token);
            }

            if (mappedToken != null) {
                ejbQlFilter.append(mappedToken);
            } else {
                ejbQlFilter.append(token);
            }
            ejbQlFilter.append(" ");
        }
        String filter = ejbQlFilter.toString().trim();
        if (filter.equalsIgnoreCase("true")) {
            return null;
        } else {
            return filter;
        }
    }

    private static enum TokenType {
        WHITESPACE, SYMBOL, NORMAL
    }

    private List<String> tokenize(String queryFilter) {
        LinkedList<String> tokens = new LinkedList<String>();
        List bits = Collections.list(new StringTokenizer(queryFilter, " \t\n\r\f()&|<>=!~+-/*", true));

        boolean inWitespace = false;
        String currentSymbol = "";
        for (int i = 0; i < bits.size(); i++) {
            TokenType tokenType;
            String bit = (String) bits.get(i);
            switch (bit.charAt(0)) {
                case ' ':
                case '\t':
                case '\n':
                case '\r':
                case '\f':
                    inWitespace = true;
                    tokenType = TokenType.WHITESPACE;
                    break;
                case '&':
                case '|':
                case '=':
                case '>':
                case '<':
                case '!':
                    // symbols are blindly coalesced so you can end up with nonsence like +-=+
                    currentSymbol += bit.charAt(0);
                    tokenType = TokenType.SYMBOL;
                    break;
                default:
                    tokenType = TokenType.NORMAL;
            }
            if (tokenType != TokenType.WHITESPACE && inWitespace) {
                // sequences of white space are simply removed
                inWitespace = false;
            }
            if (tokenType != TokenType.SYMBOL && currentSymbol.length() > 0) {
                tokens.add(currentSymbol);
                currentSymbol = "";
            }
            if (tokenType == TokenType.NORMAL) {
                tokens.add(bit);
            }
        }
        // add saved symobl if we have one
        if (currentSymbol.length() > 0) {
            tokens.add(currentSymbol);
            currentSymbol = "";
        }
        // strip off leading space
        if (tokens.getFirst().equals(" ")) {
            tokens.removeFirst();
        }
        return tokens;
    }

    private class SunColumnName{
        private final String table;
        private final String column;

        public SunColumnName(ColumnName columnName) {
            this(columnName.getvalue());
        }

        public SunColumnName(String fullName) {
            int dot = fullName.indexOf('.');
            if (dot > 0) {
                table = fullName.substring(0, dot);
                column = fullName.substring(dot + 1);
            } else {
                table = null;
                column = fullName;
            }
        }
    }

    private class EntityData {
        private final Entity entity;
        private final Map<String, Id> ids = new TreeMap<String, Id>();
        private final Map<String, Field> fields = new TreeMap<String, Field>();
        private final Map<String, RelationField> relations = new TreeMap<String, RelationField>();

        public EntityData(Entity entity) {
            if (entity == null) throw new NullPointerException("entity is null");
            this.entity = entity;

            Attributes attributes = entity.getAttributes();
            if (attributes != null) {
                for (Id id : attributes.getId()) {
                    String name = id.getName();
                    ids.put(name, id);
                    fields.put(name, id);
                }

                for (Basic basic : attributes.getBasic()) {
                    String name = basic.getName();
                    fields.put(name, basic);
                }

                for (RelationField relationField : attributes.getOneToOne()) {
                    String name = relationField.getName();
                    relations.put(name, relationField);
                }

                for (RelationField relationField : attributes.getOneToMany()) {
                    String name = relationField.getName();
                    relations.put(name, relationField);
                }

                for (RelationField relationField : attributes.getManyToOne()) {
                    String name = relationField.getName();
                    relations.put(name, relationField);
                }

                for (RelationField relationField : attributes.getManyToMany()) {
                    String name = relationField.getName();
                    relations.put(name, relationField);
                }
            }

            for (AttributeOverride attributeOverride : entity.getAttributeOverride()) {
                String name = attributeOverride.getName();
                fields.put(name, attributeOverride);
            }
        }

        public boolean hasPkColumnMapping(String column) {
            for (Id id : ids.values()) {
                if (column.equals(id.getColumn().getName())) {
                    return true;
                }
            }
            return false;
        }
    }
}
