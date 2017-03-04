package codes.recursive.blog

class PostTag {

	static belongsTo = [post: Post, tag: Tag]

	static mapping = {
		id generator: "native"
		version false
	}
}
