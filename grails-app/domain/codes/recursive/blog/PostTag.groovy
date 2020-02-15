package codes.recursive.blog

import grails.util.Holders

class PostTag {

	static belongsTo = [post: Post, tag: Tag]

	static mapping = {
		id generator:'sequence', params: [ sequence: Holders.config.codes.recursive.oracle.sequence.postTag ]
		version false
	}
}
