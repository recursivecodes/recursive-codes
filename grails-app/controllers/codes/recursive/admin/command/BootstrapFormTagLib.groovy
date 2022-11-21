package codes.recursive.admin.command

import groovy.xml.MarkupBuilder
import org.apache.commons.lang.StringEscapeUtils
import org.grails.plugins.web.taglib.ValidationTagLib

/**
 * Wrapper for whatever HTML markup we have to currently use
 * to keep Bootstrap happy
 *
 * */
class BootstrapFormTagLib extends ValidationTagLib {

    static namespace="bootform"
    static excludedProps=["dateCreated","lastUpdated","metaClass","class","validationSkipMap","gormPersistentEntity","properties","gormDynamicFinders","all","domainClass","attached","constraints","version","validationErrorsMap","dirtyPropertyNames","errors","dirty","count"]

    def inputGroupClass
    def labelColumnClass
    def labelOffsetClass
    def controlColumnClass
    def formGroupClass

    def horizontalField = { attrs, body ->
        inputGroupClass = attrs.inputGroupClass ?: 'input-group'
        labelColumnClass = attrs.labelColumnClass ?: 'col-sm-4'
        labelOffsetClass = attrs.labelOffsetClass ?: 'col-sm-offset-4'
        controlColumnClass = attrs.controlColumnClass ?: 'col-sm-7'

        def showLabel = attrs.showLabel == 'false' ? false : true
        def hfl = showLabel ? horizontalFieldLabel( attrs, body ) : ''
        out << fieldContainer( attrs,
                hfl
                        << horizontalFieldControls( attrs, body() << ( fieldHelp( attrs, body ) ) )
        )
    }

    def field = { attrs, body ->
        inputGroupClass = attrs.inputGroupClass ?: 'input-group'
        formGroupClass = attrs.formGroupClass ?: 'form-group'

        def field = attrs.field
        def bean = attrs.bean
        def label = attrs.label ?: ''
        def required = ( ( attrs?.required && attrs.required.toString() == 'true' ) ? true : false )
        def labelSrOnly = attrs.labelSrOnly

        if ( !field ) {
            throw new Exception( "'field' is required" )
        }

        out << '<div class="' + formGroupClass + hasErrors(bean: bean, field: field, ' has-error') + '" ' + (required ? 'required' : '') +'>'
        if (label) {
            out << '<label class="control-label' + ( required ? '' : ' optional-label' ) + (labelSrOnly ? ' sr-only' : '') + '" for="' + (attrs.id ? attrs.id : field) + '">'
        }
        if ( label && required ) {
            out << '<span class="required-indicator">*</span>\n\t'
        }
        if (label) {
            out << label
            out << '</label>'
        }
        out << body() << fieldHelp( attrs, body )
        out << '</div>'
    }

    def checkboxContainer = { attrs, body ->
        attrs.type = 'form-check-input'
        out << checkboxRadioContainer( attrs, body() )
    }


    def radioContainer = { attrs, body ->
        attrs.type = 'form-check-input'
        out << checkboxRadioContainer( attrs, body() )
    }

    def checkboxRadioContainer = { attrs, body ->
        def bean = attrs.bean
        def crClass = attrs.type ?: 'form-check-input'
        def horizontal = attrs.horizontal.toString() == 'true' ? true : false
        labelOffsetClass = attrs.labelOffsetClass ?: 'offset-sm-4'
        controlColumnClass = attrs.controlColumnClass ?: 'col-sm-8'
        def disabled = attrs.disabled.toString() == 'true' ? 'disabled' : ''

        if( horizontal ){
            out << '<div class="' + labelOffsetClass + ' ' + controlColumnClass + '">'
        }
        out << '<div class="' + crClass + '"' + disabled + '>'
        out << '<label>'
        out << body()
        out << '</label>'
        out << '</div>'
        if( horizontal ){
            out << '</div>'
        }
    }

    def checkboxHelp = { attrs, body ->
        out << checkboxRadioHelp( attrs, body() )
    }


    def radioHelp = { attrs, body ->
        out << checkboxRadioHelp( attrs, body() )
    }

    def checkboxRadioHelp = { attrs, body ->
        def bean = attrs.bean
        def field = attrs.field
        def horizontal = attrs.horizontal.toString() == 'true' ? true : false
        def description = attrs.description

        if ( !field ) {
            throw new Exception( "'field' is required" )
        }

        if ( !description && !bean?.errors?.hasFieldErrors( field ) ) {
            return
        }

        def hasErrors = bean?.errors?.hasFieldErrors( field ) ? 'with-errors' : ''

        if( horizontal ){
            out << '<div class="' + labelOffsetClass + ' ' + controlColumnClass + '">'
        }
        out << '<div>'

        out << '\n\t\t<span class="help-block ' + hasErrors +'">'

        if ( bean?.errors?.hasFieldErrors( field ) ) {
            renderErrorsWithClass( [ bean: bean, field: field ], body, out )
        } else {
            out << description
        }

        out << '\n\t\t</span>'

        out << '</div>'
        if( horizontal ){
            out << '</div>'
        }
    }

    def horizontalButtonContainer = { attrs, body ->
        def formGroupClass = attrs.formGroupClass ?: 'form-group'
        def labelColumnClass = attrs.labelColumnClass ?: 'col-sm-4'
        def controlColumnClass = attrs.controlColumnClass ?: 'col-sm-7'

        out << '<div class="' + formGroupClass + '">'
        out << '<div class="control-label ' + labelColumnClass +'">&nbsp;</div>'
        out << '<div class="' + controlColumnClass + '">'
        out << body()
        out << '</div>'
        out << '</div>'
    }

    def fieldContainer = { attrs, body ->
        def bean = attrs.bean
        String field = attrs.field
        String className = attrs.className ?: "form-group"

        def required = ( ( attrs?.required && attrs.required ) ? true : false )

        if ( !field ) {
            throw new Exception( "'field' is required" )
        }

        String dataAttribs = ''
        attrs.eachWithIndex{String k, Object v, i ->
            if ( k.startsWith('data-') ) {
                dataAttribs += ' ' + k + '="' + v + '"'
            }
        }
        out << "\n<div ${dataAttribs} class=\"row mb-3 ${className} ${attrs.class} ${hasErrors(bean: bean, field: field, 'has-error')} ${required ? 'required' : '' }\">\n\t"
        out << body()
        out << '\n</div>'
    }

    def inputGroup = { attrs, body ->
        def userClass = attrs.userClass
        out << "<div class=\"" + inputGroupClass + " " + userClass +  "\">\n\t\t"
        out << body()
        out << '</div>'
    }

    def horizontalFieldLabel = { attrs, body ->
        def bean = attrs.bean
        String field = attrs.field
        String label = attrs.label ?: ''
        def required = ( ( attrs?.required && attrs.required.toString() == 'true' ) ? true : false )

        if ( !field || !label ) {
            throw new Exception( "'field', and 'label' are required" )
        }


        out << "<label class=\"" + labelColumnClass + ( required ? '' : ' optional-label' ) + " control-label\" for=\"${attrs.id ?: field}\">\n\t\t"
        if ( required ) {
            out << '<span class="required-indicator">*</span>\n\t'
        }
        out << label


        out << '</label>'
    }

    def horizontalFieldControls = { attrs, body ->
        String className = attrs.className ?: controlColumnClass

        out << "\n\t<div class=\"${className}\">\n"
        out << body()
        out << '\n\t</div>'
    }


    def fieldHelp = { attrs, body ->
        def bean = attrs.bean
        String field = attrs.field
        String description = attrs.description

        if ( !field ) {
            throw new Exception( "'field' is required" )
        }

        if ( !description && !bean?.errors?.hasFieldErrors( field ) ) {
            return
        }

        out << '\n\t\t<small class="help-block">'

        if ( bean?.errors?.hasFieldErrors( field ) ) {
            renderErrorsWithClass( [ bean: bean, field: field ], body, out )
        } else {
            out << description
        }

        out << '\n\t\t</small>'
    }

    def inputAddon = { attrs, body ->
        def before = attrs.before?.toString()
        def after = attrs.after?.toString()

        out << """
            <div class="input-group">
        """

        if ( before ) {
            out << """
                <div class="input-group-addon" style="${attrs.beforeWidth ? "width:${attrs.beforeWidth}px;text-align:left": ''}">${before}</div>
            """
        }

        out << body()

        if ( after ) {
            out << """
                <div class="input-group-addon" style="${attrs.afterWidth ? "width:${attrs.afterWidth}px;text-align:left": ''}">${after}</div>
            """
        }
        out << """
            </div>
        """
    }

    Closure renderErrors = { attrs, body ->
        def renderAs = attrs.remove('as') ?: 'list'

        if (renderAs == 'list') {
            def codec = attrs.codec ?: 'HTML'
            if (codec == 'none') codec = ''

            def errorsList = extractErrors(attrs)
            if (errorsList) {
                out << '\n<ul class="error-list">'
                out << eachErrorInternalForList(attrs, errorsList, {
                    out << "<li>${message(error:it, encodeAs:codec)}</li>"
                })
                out << "</ul>"
            }
        }
        else if (renderAs.equalsIgnoreCase("xml")) {
            def mkp = new MarkupBuilder(out)
            mkp.errors() {
                eachErrorInternal(attrs, {
                    error(object: it.objectName,
                            field: it.field,
                            message: message(error:it)?.toString(),
                            'rejected-value': StringEscapeUtils.escapeXml(it.rejectedValue))
                })
            }
        }
    }

    Closure renderErrorsWithClass = { attrs, body, out ->
        def renderAs = attrs.remove('as')
        if (!renderAs) renderAs = 'list'
        if (renderAs == 'list') {
            def codec = attrs.codec ?: 'HTML'
            if (codec == 'none') codec = ''

            def errorsList = extractErrors(attrs)
            if (errorsList) {
                out << '\n<ul class="error-list">'
                out << eachErrorInternalForList(attrs, errorsList, {
                    out << "\n<li>${message(error:it, encodeAs:codec)}</li>"
                })
                out << '\n</ul>'
            }
        }
        else if (renderAs.equalsIgnoreCase("xml")) {
            def mkp = new MarkupBuilder(out)
            mkp.errors() {
                eachErrorInternal(attrs, {
                    error(object: it.objectName,
                            field: it.field,
                            message: message(error:it)?.toString(),
                            'rejected-value': StringEscapeUtils.escapeXml(it.rejectedValue))
                })
            }
        }
    }


}
