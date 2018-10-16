/*     */ package org.pentaho.di.core.database;
/*     */ 
/*     */ import org.pentaho.di.core.Const;
/*     */ import org.pentaho.di.core.row.ValueMetaInterface;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Hive2SQLDatabaseMeta
/*     */   extends BaseDatabaseMeta
/*     */   implements DatabaseInterface
/*     */ {
/*     */   public String getExtraOptionSeparator()
/*     */   {
/*  41 */     return "&";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getExtraOptionIndicator()
/*     */   {
/*  49 */     return "?";
/*     */   }
/*     */   
/*     */   public int[] getAccessTypeList()
/*     */   {
/*  54 */     return new int[] { 0, 1, 4 };
/*     */   }
/*     */   
/*     */   public int getDefaultDatabasePort()
/*     */   {
/*  59 */     if (getAccessType() == 0) {
/*  60 */       return 5432;
/*     */     }
/*  62 */     return -1;
/*     */   }
/*     */   
/*     */   public String getDriverClass()
/*     */   {
/*  67 */     if (getAccessType() == 1) {
/*  68 */       return "sun.jdbc.odbc.JdbcOdbcDriver";
/*     */     }
/*  70 */     return "org.apache.hive.jdbc.HiveDriver";
/*     */   }
/*     */   
/*     */ 
/*     */   public String getURL(String hostname, String port, String databaseName)
/*     */   {
/*  76 */     if (getAccessType() == 1) {
/*  77 */       return "jdbc:odbc:" + databaseName;
/*     */     }
/*  79 */     return "jdbc:hive2://" + hostname + ":" + port + "/" + databaseName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isFetchSizeSupported()
/*     */   {
/*  90 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean supportsBitmapIndex()
/*     */   {
/*  98 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean supportsSynonyms()
/*     */   {
/* 106 */     return false;
/*     */   }
/*     */   
/*     */   public boolean supportsSequences()
/*     */   {
/* 111 */     return true;
/*     */   }
/*     */   
/*     */   public boolean supportsSequenceNoMaxValueOption()
/*     */   {
/* 116 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean supportsAutoInc()
/*     */   {
/* 126 */     return true;
/*     */   }
/*     */   
/*     */   public String getLimitClause(int nrRows)
/*     */   {
/* 131 */     return " limit " + nrRows;
/*     */   }
/*     */   
/*     */   public String getSQLQueryFields(String tableName)
/*     */   {
/* 136 */     return "SELECT * FROM " + tableName + getLimitClause(1);
/*     */   }
/*     */   
/*     */   public String getSQLTableExists(String tablename)
/*     */   {
/* 141 */     return getSQLQueryFields(tablename);
/*     */   }
/*     */   
/*     */   public String getSQLColumnExists(String columnname, String tablename)
/*     */   {
/* 146 */     return getSQLQueryColumnFields(columnname, tablename);
/*     */   }
/*     */   
/*     */   public String getSQLQueryColumnFields(String columnname, String tableName) {
/* 150 */     return "SELECT " + columnname + " FROM " + tableName + getLimitClause(1);
/*     */   }
/*     */   
/*     */   public boolean needsToLockAllTables()
/*     */   {
/* 155 */     return false;
/*     */   }
/*     */   
/*     */   public String getSQLListOfSequences()
/*     */   {
/* 160 */     return "SELECT relname AS sequence_name FROM pg_catalog.pg_statio_all_sequences";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSQLNextSequenceValue(String sequenceName)
/*     */   {
/* 172 */     return "SELECT nextval('" + sequenceName + "')";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSQLCurrentSequenceValue(String sequenceName)
/*     */   {
/* 184 */     return "SELECT currval('" + sequenceName + "')";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSQLSequenceExists(String sequenceName)
/*     */   {
/* 196 */     return 
/* 197 */       "SELECT relname AS sequence_name FROM pg_catalog.pg_statio_all_sequences WHERE relname = '" + sequenceName.toLowerCase() + "'";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getAddColumnStatement(String tablename, ValueMetaInterface v, String tk, boolean use_autoinc, String pk, boolean semicolon)
/*     */   {
/* 220 */     return "ALTER TABLE " + tablename + " ADD COLUMN " + getFieldDefinition(v, tk, pk, use_autoinc, true, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDropColumnStatement(String tablename, ValueMetaInterface v, String tk, boolean use_autoinc, String pk, boolean semicolon)
/*     */   {
/* 243 */     return "ALTER TABLE " + tablename + " DROP COLUMN " + v.getName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getModifyColumnStatement(String tablename, ValueMetaInterface v, String tk, boolean use_autoinc, String pk, boolean semicolon)
/*     */   {
/* 266 */     String retval = "";
/*     */     
/* 268 */     ValueMetaInterface tmpColumn = v.clone();
/*     */     
/* 270 */     String tmpName = v.getName();
/* 271 */     boolean isQuoted = (tmpName.startsWith(getStartQuote())) && (tmpName.endsWith(getEndQuote()));
/* 272 */     if (isQuoted)
/*     */     {
/*     */ 
/* 275 */       tmpName = tmpName.substring(1, tmpName.length() - 1);
/*     */     }
/*     */     
/* 278 */     tmpName = tmpName + "_KTL";
/*     */     
/*     */ 
/*     */ 
/* 282 */     if (isQuoted) {
/* 283 */       tmpName = getStartQuote() + tmpName + getEndQuote();
/*     */     }
/* 285 */     tmpColumn.setName(tmpName);
/*     */     
/*     */ 
/* 288 */     retval = retval + getAddColumnStatement(tablename, tmpColumn, tk, use_autoinc, pk, semicolon) + ";" + Const.CR;
/*     */     
/* 290 */     retval = retval + "UPDATE " + tablename + " SET " + tmpColumn.getName() + "=" + v.getName() + ";" + Const.CR;
/*     */     
/* 292 */     retval = retval + getDropColumnStatement(tablename, v, tk, use_autoinc, pk, semicolon) + ";" + Const.CR;
/*     */     
/* 294 */     retval = retval + "ALTER TABLE " + tablename + " RENAME " + tmpColumn.getName() + " TO " + v.getName() + ";" + Const.CR;
/* 295 */     return retval;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getFieldDefinition(ValueMetaInterface v, String tk, String pk, boolean use_autoinc, boolean add_fieldname, boolean add_cr)
/*     */   {
/* 301 */     String retval = "";
/*     */     
/* 303 */     String fieldname = v.getName();
/* 304 */     int length = v.getLength();
/* 305 */     int precision = v.getPrecision();
/*     */     
/* 307 */     if (add_fieldname) {
/* 308 */       retval = retval + fieldname + " ";
/*     */     }
/*     */     
/* 311 */     int type = v.getType();
/* 312 */     switch (type) {
/*     */     case 3: 
/*     */     case 9: 
/* 315 */       retval = retval + "TIMESTAMP";
/* 316 */       break;
/*     */     case 4: 
/* 318 */       if (supportsBooleanDataType()) {
/* 319 */         retval = retval + "BOOLEAN";
/*     */       } else {
/* 321 */         retval = retval + "CHAR(1)";
/*     */       }
/* 323 */       break;
/*     */     case 1: 
/*     */     case 5: 
/*     */     case 6: 
/* 327 */       if ((fieldname.equalsIgnoreCase(tk)) || 
/* 328 */         (fieldname.equalsIgnoreCase(pk)))
/*     */       {
/* 330 */         retval = retval + "BIGSERIAL";
/*     */       }
/* 332 */       else if (length > 0) {
/* 333 */         if ((precision > 0) || (length > 18))
/*     */         {
/* 335 */           retval = retval + "NUMERIC(" + (length + precision) + ", " + precision + ")";
/*     */         }
/* 337 */         else if (length > 9) {
/* 338 */           retval = retval + "BIGINT";
/*     */         }
/* 340 */         else if (length < 5) {
/* 341 */           retval = retval + "SMALLINT";
/*     */         } else {
/* 343 */           retval = retval + "INTEGER";
/*     */         }
/*     */         
/*     */ 
/*     */       }
/*     */       else {
/* 349 */         retval = retval + "DOUBLE PRECISION";
/*     */       }
/*     */       
/* 352 */       break;
/*     */     case 2: 
/* 354 */       if ((length < 1) || (length >= 9999999)) {
/* 355 */         retval = retval + "TEXT";
/*     */       } else {
/* 357 */         retval = retval + "VARCHAR(" + length + ")";
/*     */       }
/* 359 */       break;
/*     */     case 7: case 8: default: 
/* 361 */       retval = retval + " UNKNOWN";
/*     */     }
/*     */     
/*     */     
/* 365 */     if (add_cr) {
/* 366 */       retval = retval + Const.CR;
/*     */     }
/*     */     
/* 369 */     return retval;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSQLListOfProcedures()
/*     */   {
/* 379 */     return 
/* 380 */       "select proname from pg_proc, pg_user where pg_user.usesysid = pg_proc.proowner and upper(pg_user.usename) = '" + getUsername().toUpperCase() + "' order by proname";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getReservedWords()
/*     */   {
/* 390 */     return new String[] { "A", "ABORT", "ABS", "ABSOLUTE", "ACCESS", "ACTION", "ADA", "ADD", "ADMIN", "AFTER", "AGGREGATE", "ALIAS", "ALL", "ALLOCATE", "ALSO", "ALTER", "ALWAYS", "ANALYSE", "ANALYZE", "AND", "ANY", "ARE", "ARRAY", "AS", "ASC", "ASENSITIVE", "ASSERTION", "ASSIGNMENT", "ASYMMETRIC", "AT", "ATOMIC", "ATTRIBUTE", "ATTRIBUTES", "AUTHORIZATION", "AVG", "BACKWARD", "BEFORE", "BEGIN", "BERNOULLI", "BETWEEN", "BIGINT", "BINARY", "BIT", "BITVAR", "BIT_LENGTH", "BLOB", "BOOLEAN", "BOTH", "BREADTH", "BY", "C", "CACHE", "CALL", "CALLED", "CARDINALITY", "CASCADE", "CASCADED", "CASE", "CAST", "CATALOG", "CATALOG_NAME", "CEIL", "CEILING", "CHAIN", "CHAR", "CHARACTER", "CHARACTERISTICS", "CHARACTERS", "CHARACTER_LENGTH", "CHARACTER_SET_CATALOG", "CHARACTER_SET_NAME", "CHARACTER_SET_SCHEMA", "CHAR_LENGTH", "CHECK", "CHECKED", "CHECKPOINT", "CLASS", "CLASS_ORIGIN", "CLOB", "CLOSE", "CLUSTER", "COALESCE", "COBOL", "COLLATE", "COLLATION", "COLLATION_CATALOG", "COLLATION_NAME", "COLLATION_SCHEMA", "COLLECT", "COLUMN", "COLUMN_NAME", "COMMAND_FUNCTION", "COMMAND_FUNCTION_CODE", "COMMENT", "COMMIT", "COMMITTED", "COMPLETION", "CONDITION", "CONDITION_NUMBER", "CONNECT", "CONNECTION", "CONNECTION_NAME", "CONSTRAINT", "CONSTRAINTS", "CONSTRAINT_CATALOG", "CONSTRAINT_NAME", "CONSTRAINT_SCHEMA", "CONSTRUCTOR", "CONTAINS", "CONTINUE", "CONVERSION", "CONVERT", "COPY", "CORR", "CORRESPONDING", "COUNT", "COVAR_POP", "COVAR_SAMP", "CREATE", "CREATEDB", "CREATEROLE", "CREATEUSER", "CROSS", "CSV", "CUBE", "CUME_DIST", "CURRENT", "CURRENT_DATE", "CURRENT_DEFAULT_TRANSFORM_GROUP", "CURRENT_PATH", "CURRENT_ROLE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_TRANSFORM_GROUP_FOR_TYPE", "CURRENT_USER", "CURSOR", "CURSOR_NAME", "CYCLE", "DATA", "DATABASE", "DATE", "DATETIME_INTERVAL_CODE", "DATETIME_INTERVAL_PRECISION", "DAY", "DEALLOCATE", "DEC", "DECIMAL", "DECLARE", "DEFAULT", "DEFAULTS", "DEFERRABLE", "DEFERRED", "DEFINED", "DEFINER", "DEGREE", "DELETE", "DELIMITER", "DELIMITERS", "DENSE_RANK", "DEPTH", "DEREF", "DERIVED", "DESC", "DESCRIBE", "DESCRIPTOR", "DESTROY", "DESTRUCTOR", "DETERMINISTIC", "DIAGNOSTICS", "DICTIONARY", "DISABLE", "DISCONNECT", "DISPATCH", "DISTINCT", "DO", "DOMAIN", "DOUBLE", "DROP", "DYNAMIC", "DYNAMIC_FUNCTION", "DYNAMIC_FUNCTION_CODE", "EACH", "ELEMENT", "ELSE", "ENABLE", "ENCODING", "ENCRYPTED", "END", "END-EXEC", "EQUALS", "ESCAPE", "EVERY", "EXCEPT", "EXCEPTION", "EXCLUDE", "EXCLUDING", "EXCLUSIVE", "EXEC", "EXECUTE", "EXISTING", "EXISTS", "EXP", "EXPLAIN", "EXTERNAL", "EXTRACT", "FALSE", "FETCH", "FILTER", "FINAL", "FIRST", "FLOAT", "FLOOR", "FOLLOWING", "FOR", "FORCE", "FOREIGN", "FORTRAN", "FORWARD", "FOUND", "FREE", "FREEZE", "FROM", "FULL", "FUNCTION", "FUSION", "G", "GENERAL", "GENERATED", "GET", "GLOBAL", "GO", "GOTO", "GRANT", "GRANTED", "GREATEST", "GROUP", "GROUPING", "HANDLER", "HAVING", "HEADER", "HIERARCHY", "HOLD", "HOST", "HOUR", "IDENTITY", "IGNORE", "ILIKE", "IMMEDIATE", "IMMUTABLE", "IMPLEMENTATION", "IMPLICIT", "IN", "INCLUDING", "INCREMENT", "INDEX", "INDICATOR", "INFIX", "INHERIT", "INHERITS", "INITIALIZE", "INITIALLY", "INNER", "INOUT", "INPUT", "INSENSITIVE", "INSERT", "INSTANCE", "INSTANTIABLE", "INSTEAD", "INT", "INTEGER", "INTERSECT", "INTERSECTION", "INTERVAL", "INTO", "INVOKER", "IS", "ISNULL", "ISOLATION", "ITERATE", "JOIN", "K", "KEY", "KEY_MEMBER", "KEY_TYPE", "LANCOMPILER", "LANGUAGE", "LARGE", "LAST", "LATERAL", "LEADING", "LEAST", "LEFT", "LENGTH", "LESS", "LEVEL", "LIKE", "LIMIT", "LISTEN", "LN", "LOAD", "LOCAL", "LOCALTIME", "LOCALTIMESTAMP", "LOCATION", "LOCATOR", "LOCK", "LOGIN", "LOWER", "M", "MAP", "MATCH", "MATCHED", "MAX", "MAXVALUE", "MEMBER", "MERGE", "MESSAGE_LENGTH", "MESSAGE_OCTET_LENGTH", "MESSAGE_TEXT", "METHOD", "MIN", "MINUTE", "MINVALUE", "MOD", "MODE", "MODIFIES", "MODIFY", "MODULE", "MONTH", "MORE", "MOVE", "MULTISET", "MUMPS", "NAME", "NAMES", "NATIONAL", "NATURAL", "NCHAR", "NCLOB", "NESTING", "NEW", "NEXT", "NO", "NOCREATEDB", "NOCREATEROLE", "NOCREATEUSER", "NOINHERIT", "NOLOGIN", "NONE", "NORMALIZE", "NORMALIZED", "NOSUPERUSER", "NOT", "NOTHING", "NOTIFY", "NOTNULL", "NOWAIT", "NULL", "NULLABLE", "NULLIF", "NULLS", "NUMBER", "NUMERIC", "OBJECT", "OCTETS", "OCTET_LENGTH", "OF", "OFF", "OFFSET", "OIDS", "OLD", "ON", "ONLY", "OPEN", "OPERATION", "OPERATOR", "OPTION", "OPTIONS", "OR", "ORDER", "ORDERING", "ORDINALITY", "OTHERS", "OUT", "OUTER", "OUTPUT", "OVER", "OVERLAPS", "OVERLAY", "OVERRIDING", "OWNER", "PAD", "PARAMETER", "PARAMETERS", "PARAMETER_MODE", "PARAMETER_NAME", "PARAMETER_ORDINAL_POSITION", "PARAMETER_SPECIFIC_CATALOG", "PARAMETER_SPECIFIC_NAME", "PARAMETER_SPECIFIC_SCHEMA", "PARTIAL", "PARTITION", "PASCAL", "PASSWORD", "PATH", "PERCENTILE_CONT", "PERCENTILE_DISC", "PERCENT_RANK", "PLACING", "PLI", "POSITION", "POSTFIX", "POWER", "PRECEDING", "PRECISION", "PREFIX", "PREORDER", "PREPARE", "PREPARED", "PRESERVE", "PRIMARY", "PRIOR", "PRIVILEGES", "PROCEDURAL", "PROCEDURE", "PUBLIC", "QUOTE", "RANGE", "RANK", "READ", "READS", "REAL", "RECHECK", "RECURSIVE", "REF", "REFERENCES", "REFERENCING", "REGR_AVGX", "REGR_AVGY", "REGR_COUNT", "REGR_INTERCEPT", "REGR_R2", "REGR_SLOPE", "REGR_SXX", "REGR_SXY", "REGR_SYY", "REINDEX", "RELATIVE", "RELEASE", "RENAME", "REPEATABLE", "REPLACE", "RESET", "RESTART", "RESTRICT", "RESULT", "RETURN", "RETURNED_CARDINALITY", "RETURNED_LENGTH", "RETURNED_OCTET_LENGTH", "RETURNED_SQLSTATE", "RETURNS", "REVOKE", "RIGHT", "ROLE", "ROLLBACK", "ROLLUP", "ROUTINE", "ROUTINE_CATALOG", "ROUTINE_NAME", "ROUTINE_SCHEMA", "ROW", "ROWS", "ROW_COUNT", "ROW_NUMBER", "RULE", "SAVEPOINT", "SCALE", "SCHEMA", "SCHEMA_NAME", "SCOPE", "SCOPE_CATALOG", "SCOPE_NAME", "SCOPE_SCHEMA", "SCROLL", "SEARCH", "SECOND", "SECTION", "SECURITY", "SELECT", "SELF", "SENSITIVE", "SEQUENCE", "SERIALIZABLE", "SERVER_NAME", "SESSION", "SESSION_USER", "SET", "SETOF", "SETS", "SHARE", "SHOW", "SIMILAR", "SIMPLE", "SIZE", "SMALLINT", "SOME", "SOURCE", "SPACE", "SPECIFIC", "SPECIFICTYPE", "SPECIFIC_NAME", "SQL", "SQLCODE", "SQLERROR", "SQLEXCEPTION", "SQLSTATE", "SQLWARNING", "SQRT", "STABLE", "START", "STATE", "STATEMENT", "STATIC", "STATISTICS", "STDDEV_POP", "STDDEV_SAMP", "STDIN", "STDOUT", "STORAGE", "STRICT", "STRUCTURE", "STYLE", "SUBCLASS_ORIGIN", "SUBLIST", "SUBMULTISET", "SUBSTRING", "SUM", "SUPERUSER", "SYMMETRIC", "SYSID", "SYSTEM", "SYSTEM_USER", "TABLE", "TABLESAMPLE", "TABLESPACE", "TABLE_NAME", "TEMP", "TEMPLATE", "TEMPORARY", "TERMINATE", "THAN", "THEN", "TIES", "TIME", "TIMESTAMP", "TIMEZONE_HOUR", "TIMEZONE_MINUTE", "TO", "TOAST", "TOP_LEVEL_COUNT", "TRAILING", "TRANSACTION", "TRANSACTIONS_COMMITTED", "TRANSACTIONS_ROLLED_BACK", "TRANSACTION_ACTIVE", "TRANSFORM", "TRANSFORMS", "TRANSLATE", "TRANSLATION", "TREAT", "TRIGGER", "TRIGGER_CATALOG", "TRIGGER_NAME", "TRIGGER_SCHEMA", "TRIM", "TRUE", "TRUNCATE", "TRUSTED", "TYPE", "UESCAPE", "UNBOUNDED", "UNCOMMITTED", "UNDER", "UNENCRYPTED", "UNION", "UNIQUE", "UNKNOWN", "UNLISTEN", "UNNAMED", "UNNEST", "UNTIL", "UPDATE", "UPPER", "USAGE", "USER", "USER_DEFINED_TYPE_CATALOG", "USER_DEFINED_TYPE_CODE", "USER_DEFINED_TYPE_NAME", "USER_DEFINED_TYPE_SCHEMA", "USING", "VACUUM", "VALID", "VALIDATOR", "VALUE", "VALUES", "VARCHAR", "VARIABLE", "VARYING", "VAR_POP", "VAR_SAMP", "VERBOSE", "VIEW", "VOLATILE", "WHEN", "WHENEVER", "WHERE", "WIDTH_BUCKET", "WINDOW", "WITH", "WITHIN", "WITHOUT", "WORK", "WRITE", "YEAR", "ZONE" };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean supportsRepository()
/*     */   {
/* 469 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSQLLockTables(String[] tableNames)
/*     */   {
/* 479 */     String sql = "LOCK TABLE ";
/* 480 */     for (int i = 0; i < tableNames.length; i++) {
/* 481 */       if (i > 0) {
/* 482 */         sql = sql + ", ";
/*     */       }
/* 484 */       sql = sql + tableNames[i] + " ";
/*     */     }
/* 486 */     sql = sql + "IN ACCESS EXCLUSIVE MODE;" + Const.CR;
/*     */     
/* 488 */     return sql;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSQLUnlockTables(String[] tableName)
/*     */   {
/* 498 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isDefaultingToUppercase()
/*     */   {
/* 507 */     return false;
/*     */   }
/*     */   
/*     */   public String getExtraOptionsHelpText()
/*     */   {
/* 512 */     return "http://jdbc.postgresql.org/documentation/83/connect.html#connection-parameters";
/*     */   }
/*     */   
/*     */   public String[] getUsedLibraries()
/*     */   {
/* 517 */     return new String[] { "hive-jdbc-1.2.1.jar" };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean supportsErrorHandlingOnBatchUpdates()
/*     */   {
/* 525 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String quoteSQLString(String string)
/*     */   {
/* 536 */     string = string.replaceAll("'", "''");
/* 537 */     string = string.replaceAll("\\n", "\\\\n");
/* 538 */     string = string.replaceAll("\\r", "\\\\r");
/* 539 */     return "E'" + string + "'";
/*     */   }
/*     */   
/*     */   public boolean requiresCastToVariousForIsNull()
/*     */   {
/* 544 */     return true;
/*     */   }
/*     */   
/*     */   public boolean supportsGetBlob()
/*     */   {
/* 549 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean useSafePoints()
/*     */   {
/* 558 */     return true;
/*     */   }
/*     */ }

/* Location:           E:\m2\repository\pentaho-kettle\kettle-core\7.1.0.0-12\kettle-core-7.1.0.0-12.jar
 * Qualified Name:     org.pentaho.di.core.database.PostgreSQLDatabaseMeta
 * Java Class Version: 8 (52.0)
 * JD-Core Version:    0.7.1
 */