databaseChangeLog = {

    changeSet(author: "toddsharp", id: "2017031900-add-post-keywords") {
        addColumn(tableName: 'post') {
            column(name: 'keywords', type: 'NVARCHAR(500)') {
                constraints(nullable: true)
            }
        }
        addColumn(tableName: 'post') {
            column(name: 'summary', type: 'NVARCHAR(500)') {
                constraints(nullable: true)
            }
        }
    }
}
