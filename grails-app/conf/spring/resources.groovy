package conf.spring

import codes.recursive.Util
import groovy.sql.Sql

// Place your Spring DSL code here
beans = {
    sql(Sql, ref('dataSource')) {}
    util(Util){}
}
