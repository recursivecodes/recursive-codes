package codes.recursive

import grails.util.Holders
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode(includes='authority')
@ToString(includes='authority', includeNames=true, includePackage=false)
class Role implements Serializable {

	static final String ROLE_ADMIN = 'ROLE_ADMIN'
	static final String ROLE_AUTHOR = 'ROLE_AUTHOR'
	private static final long serialVersionUID = 1

	String authority

	static constraints = {
		authority blank: false, unique: true
	}

	static mapping = {
		id generator:'sequence', params: [ sequence: Holders.config.codes.recursive.oracle.sequence.role ]
		cache true
	}
}
