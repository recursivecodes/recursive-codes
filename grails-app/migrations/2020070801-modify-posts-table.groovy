databaseChangeLog = {
    changeSet(author: "toddsharp", id: "2020070801-modify-posts-table") {
        renameColumn(tableName: 'post', newColumnName: 'banner_img', oldColumnName: 'bannerImg')
    }
}
