databaseChangeLog = {
    changeSet(author: "toddsharp", id: "2020070800-modify-posts-table") {
        addColumn(tableName: 'post') {
            column(name: 'bannerImg', type: 'VARCHAR2(2000)') {
                constraints(nullable: true)
            }
        }
    }
}
