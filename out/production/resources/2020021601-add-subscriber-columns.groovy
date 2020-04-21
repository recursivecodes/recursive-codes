databaseChangeLog = {
    changeSet(author: "toddsharp", id: "2020021601-add-subscriber-columns") {
        addColumn(tableName: 'subscriber') {
            column(name: 'verification_token', type: 'NVARCHAR(36)') {
                constraints(nullable: true)
            }
            column(name: 'is_verified', type: 'BIT', defaultValue: 0) {
                constraints(nullable: false)
            }
        }
    }
}
