databaseChangeLog = {
    include(file: '2017022501-create-user-tables.groovy')
    include(file: '2017022502-create-blog-tables.groovy')
    include(file: '2017022503-insert-default-roles.groovy')
    include(file: '2017031900-add-post-keywords.groovy')
}
