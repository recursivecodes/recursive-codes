package conf.spring

import groovy.sql.Sql

// Place your Spring DSL code here
beans = {
    sql(Sql, ref('dataSource')) {}
}
