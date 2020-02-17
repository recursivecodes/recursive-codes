databaseChangeLog = {
    include(file: '2017022501-create-user-tables.groovy')
    include(file: '2017022502-create-blog-tables.groovy')
    include(file: '2017022503-insert-default-roles.groovy')
    include(file: '2017031900-add-post-keywords.groovy')
    include(file: '2018080200-create-sitemap-table.groovy')
    include(file: '2020021400-add-import-columns.groovy')
    include(file: '2020021600-add-subscribers-table.groovy')
    include(file: '2020021601-add-subscriber-columns.groovy')
}
