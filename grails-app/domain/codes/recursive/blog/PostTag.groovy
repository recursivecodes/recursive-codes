package codes.recursive.blog

class PostTag {

	static belongsTo = [post: Post, tag: Tag]

	static mapping = {
		id generator:'sequence', params: [ sequence: 'ISEQ$$_33768' ]
		version false
	}
}
