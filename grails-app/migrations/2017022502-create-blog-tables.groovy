databaseChangeLog = {

    changeSet(author: "toddsharp", id: "2017022502-create-blog-tables") {
        createTable(tableName: 'post') {
            column(name: 'id', type: "INT", autoIncrement: true) {
                constraints(primaryKey: true)
            }
            column(name: 'title', type: 'NVARCHAR(500)') {
                constraints(nullable: false)
            }
            column(name: 'article', type: 'LONGTEXT') {
                constraints(nullable: false)
            }
            column(name: 'authored_by_id', type: 'INT') {
                constraints(nullable: false)
            }
            column(name: 'is_published', type: 'BIT', defaultValue: 1) {
                constraints(nullable: false)
            }
            column(name: 'version', type: 'INT', defaultValue: 0) {
                constraints(nullable: false)
            }
            column(name: 'published_date', type: 'DATETIME')
            column(name: 'date_created', type: 'DATETIME', defaultValueComputed: 'CURRENT_TIMESTAMP') {
                constraints(nullable: false)
            }
            column(name: 'last_updated', type: 'DATETIME', defaultValueComputed: 'CURRENT_TIMESTAMP') {
                constraints(nullable: false)
            }
        }

        createTable(tableName: 'tag'){
            column(name: 'id', type: "INT", autoIncrement: true) {
                constraints(primaryKey: true)
            }
            column(name: 'name', type: 'NVARCHAR(100)') {
                constraints(nullable: false)
            }
            column(name: 'version', type: 'INT', defaultValue: 0) {
                constraints(nullable: false)
            }
            column(name: 'date_created', type: 'DATETIME', defaultValueComputed: 'CURRENT_TIMESTAMP') {
                constraints(nullable: false)
            }
            column(name: 'last_updated', type: 'DATETIME', defaultValueComputed: 'CURRENT_TIMESTAMP') {
                constraints(nullable: false)
            }
        }

        createTable(tableName: 'post_tag'){
            column(name: 'id', type: "INT", autoIncrement: true) {
                constraints(primaryKey: true)
            }
            column(name: 'tag_id', type: 'INT') {
                constraints(nullable: false)
            }
            column(name: 'post_id', type: 'INT') {
                constraints(nullable: false)
            }
        }

        addForeignKeyConstraint(baseColumnNames: 'tag_id', baseTableName: 'post_tag', constraintName: 'fk_post_tag_tag', referencedColumnNames: 'id', referencedTableName: 'tag')
        addForeignKeyConstraint(baseColumnNames: 'post_id', baseTableName: 'post_tag', constraintName: 'fk_post_tag_post', referencedColumnNames: 'id', referencedTableName: 'post')
    }

}
