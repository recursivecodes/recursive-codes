databaseChangeLog = {

    changeSet(author: "toddsharp", id: "2017022501-create-user-tables") {
        createTable(tableName: 'user') {
            column(name: 'id', type: "INT", autoIncrement: true) {
                constraints(primaryKey: true)
            }
            column(name: 'username', type: 'NVARCHAR(100)') {
                constraints(nullable: false)
            }
            column(name: 'password', type: 'NVARCHAR(200)')
            column(name: 'first_name', type: 'NVARCHAR(100)') {
                constraints(nullable: false)
            }
            column(name: 'last_name', type: 'NVARCHAR(100)') {
                constraints(nullable: false)
            }
            column(name: 'enabled', type: 'BIT', defaultValue: 1) {
                constraints(nullable: false)
            }
            column(name: 'account_expired', type: 'BIT', defaultValue: 0) {
                constraints(nullable: false)
            }
            column(name: 'password_expired', type: 'BIT', defaultValue: 0) {
                constraints(nullable: false)
            }
            column(name: 'account_locked', type: 'BIT', defaultValue: 0) {
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

        createTable(tableName: 'role'){
            column(name: 'id', type: "INT", autoIncrement: true) {
                constraints(primaryKey: true)
            }
            column(name: 'authority', type: 'NVARCHAR(100)') {
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

        createTable(tableName: 'user_role'){
            column(name: 'id', type: "INT", autoIncrement: true) {
                constraints(primaryKey: true)
            }
            column(name: 'user_id', type: 'INT') {
                constraints(nullable: false)
            }
            column(name: 'role_id', type: 'INT') {
                constraints(nullable: false)
            }
        }

        addForeignKeyConstraint(baseColumnNames: 'user_id', baseTableName: 'user_role', constraintName: 'fk_user_role_user', referencedColumnNames: 'id', referencedTableName: 'user')
        addForeignKeyConstraint(baseColumnNames: 'role_id', baseTableName: 'user_role', constraintName: 'fk_user_role_role', referencedColumnNames: 'id', referencedTableName: 'role')

    }

}
