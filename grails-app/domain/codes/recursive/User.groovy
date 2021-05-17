package codes.recursive

import grails.util.Holders
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class User implements Serializable {

	private static final long serialVersionUID = 1

	transient springSecurityService

	String firstName
	String lastName
	String username
	String password
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

	def getFullName() {
		return this.firstName + ' ' + this.lastName
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this)*.role
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
	}

	static transients = ['springSecurityService']

	static constraints = {
		password blank: false, password: true
		username blank: false, unique: true
		firstName nullable: false
		lastName nullable: false
	}

	static mapping = {
		id generator:'sequence', params: [ sequence: Holders.config.codes.recursive.oracle.sequence.user ]
		table name: 'USER_'
		autowire true
	}

	static hasMany = [userRoles: UserRole]

	static searchable = {
		except = 'userRoles'
	}
}
