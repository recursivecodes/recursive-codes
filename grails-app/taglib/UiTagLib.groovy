import grails.core.GrailsApplication
import grails.util.Environment
import org.springframework.web.servlet.support.RequestContextUtils

import java.text.BreakIterator

class UiTagLib {
    static namespace = "ui"

    GrailsApplication grailsApplication

    /**
     * panel - a bootstrap panel
     * @panelClass the class used for the panel.  default=panel-primary
     * @panelId the id used for the panel.  default=uuid
     * @panelId the id used for the panel.  default=uuid
     * @showRefreshBtn should we show a refresh button in the panel header?  default=false
     * @title the text to display in the panel header
     */
    def panel = { attrs, body ->
        def pClass = attrs.panelClass ?: 'panel-primary'
        def showRefreshBtn = attrs.showRefreshBtn.toString() == 'true'
        def pId = attrs.panelId ?: UUID.randomUUID().toString().replaceAll('-', '')
        out << """
            <div class="panel ${pClass}" id="${pId}">
				<div class="panel-heading">
					<div class="pull-left">
					    <h3 class="panel-title">${attrs.title}</h3>
					</div>
					${
            showRefreshBtn ? """
                    <div class="pull-right">
					    <i class="fa fa-refresh pointer"></i>
					</div>
                    """ : ""
        }
					<div class="clearfix"></div>
				</div>

				<div class="panel-body">
					${body()}
				</div>
			</div>
        """
    }

    /**
     *
     * our own custom version of g:paginate so we can style it 'bootstrappy'
     *
     * Creates next/previous links to support pagination for the current controller.<br/>
     *
     * &lt;g:paginate total="${Account.count()}" /&gt;<br/>
     *
     * @emptyTag
     *
     * @attr total REQUIRED The total number of results to paginate
     * @attr action the name of the action to use in the link, if not specified the default action will be linked
     * @attr controller the name of the controller to use in the link, if not specified the current controller will be linked
     * @attr id The id to use in the link
     * @attr params A map containing request parameters
     * @attr prev The text to display for the previous link (defaults to "Previous" as defined by default.paginate.prev property in I18n messages.properties)
     * @attr next The text to display for the next link (defaults to "Next" as defined by default.paginate.next property in I18n messages.properties)
     * @attr omitPrev Whether to not show the previous link (if set to true, the previous link will not be shown)
     * @attr omitNext Whether to not show the next link (if set to true, the next link will not be shown)
     * @attr omitFirst Whether to not show the first link (if set to true, the first link will not be shown)
     * @attr omitLast Whether to not show the last link (if set to true, the last link will not be shown)
     * @attr max The number of records displayed per page (defaults to 10). Used ONLY if params.max is empty
     * @attr maxsteps The number of steps displayed for pagination (defaults to 10). Used ONLY if params.maxsteps is empty
     * @attr offset Used only if params.offset is empty
     * @attr mapping The named URL mapping to use to rewrite the link
     * @attr fragment The link fragment (often called anchor tag) to use
     */
    Closure paginate = { attrs ->
        def writer = out
        if (attrs.total == null) {
            throwTagError("Tag [paginate] is missing required attribute [total]")
        }

        def messageSource = grailsAttributes.messageSource
        def locale = RequestContextUtils.getLocale(request)

        def total = attrs.int('total') ?: 0
        def offset = params.int('offset') ?: 0
        def max = params.int('max')
        def maxsteps = (attrs.int('maxsteps') ?: 10)

        if (!offset) offset = (attrs.int('offset') ?: 0)
        if (!max) max = (attrs.int('max') ?: 10)

        def linkParams = [:]
        if (attrs.params) linkParams.putAll(attrs.params)
        linkParams.offset = offset - max
        linkParams.max = max
        linkParams.remove('filterUpdate')
        if (params.sort) linkParams.sort = params.sort
        if (params.order) linkParams.order = params.order

        def linkTagAttrs = [:]
        def action
        if (attrs.containsKey('mapping')) {
            linkTagAttrs.mapping = attrs.mapping
            action = attrs.action
        } else {
            action = attrs.action ?: params.action
        }
        if (action) {
            linkTagAttrs.action = action
        }
        if (attrs.controller) {
            linkTagAttrs.controller = attrs.controller
        }
        if (attrs.id != null) {
            linkTagAttrs.id = attrs.id
        }
        if (attrs.fragment != null) {
            linkTagAttrs.fragment = attrs.fragment
        }
        linkTagAttrs.params = linkParams

        writer << '<ul class="pagination">'

        // determine paging variables
        def steps = maxsteps > 0
        int currentstep = (offset / max) + 1
        int firststep = 1
        int laststep = Math.round(Math.ceil(total / max))

        // display previous link when not on firststep unless omitPrev is true
        if (currentstep > firststep && !attrs.boolean('omitPrev')) {
            linkTagAttrs.class = 'prevLink'
            linkParams.offset = offset - max
            writer << '<li>' + link(linkTagAttrs.clone()) {
                (attrs.prev ?: messageSource.getMessage('paginate.prev', null, messageSource.getMessage('default.paginate.prev', null, 'Previous', locale), locale))
            } + '</li>'
        }

        // display steps when steps are enabled and laststep is not firststep
        if (steps && laststep > firststep) {
            linkTagAttrs.class = 'step'

            // determine begin and endstep paging variables
            int beginstep = currentstep - Math.round(maxsteps / 2) + (maxsteps % 2)
            int endstep = currentstep + Math.round(maxsteps / 2) - 1

            if (beginstep < firststep) {
                beginstep = firststep
                endstep = maxsteps
            }
            if (endstep > laststep) {
                beginstep = laststep - maxsteps + 1
                if (beginstep < firststep) {
                    beginstep = firststep
                }
                endstep = laststep
            }

            // display firststep link when beginstep is not firststep
            if (beginstep > firststep && !attrs.boolean('omitFirst')) {
                linkParams.offset = 0
                writer << '<li>' + link(linkTagAttrs.clone()) { firststep.toString() } + '</li>'
            }

            //show a gap if beginstep isn't immediately after firststep, and if were not omitting first or rev
            /*
            if (beginstep > firststep+1 && (!attrs.boolean('omitFirst') || !attrs.boolean('omitPrev')) ) {
                writer << '<span class="step gap">..</span>'
            }
            */

            // display paginate steps
            (beginstep..endstep).each { i ->
                if (currentstep == i) {
                    writer << "<li class=\"active\"><a href=\"#\">${g.formatNumber(number: i, type: 'number')}</a></li>"
                } else {
                    linkParams.offset = (i - 1) * max
                    writer << '<li>' + link(linkTagAttrs.clone()) {
                        g.formatNumber(number: i, type: 'number')
                    } + '</li>'
                }
            }

            //show a gap if beginstep isn't immediately before firststep, and if were not omitting first or rev
            /*
            if (endstep+1 < laststep && (!attrs.boolean('omitLast') || !attrs.boolean('omitNext'))) {
                writer << '<span class="step gap">..</span>'
            }
            */
            // display laststep link when endstep is not laststep
            if (endstep < laststep && !attrs.boolean('omitLast')) {
                linkParams.offset = (laststep - 1) * max
                writer << '<li>' + link(linkTagAttrs.clone()) {
                    g.formatNumber(number: laststep, type: 'number')
                } + '</li>'
            }
        }

        // display next link when not on laststep unless omitNext is true
        if (currentstep < laststep && !attrs.boolean('omitNext')) {
            linkTagAttrs.class = 'nextLink'
            linkParams.offset = offset + max
            writer << '<li>' + link(linkTagAttrs.clone()) {
                (attrs.next ? attrs.next : messageSource.getMessage('paginate.next', null, messageSource.getMessage('default.paginate.next', null, 'Next', locale), locale))
            } + '</li>'
        }

        writer << '</ul>'
    }

    def errorContainer = { attrs, body ->
        attrs.id = attrs.id ?: 'error'
        attrs.alertClass = attrs.alertClass ?: 'alert-danger'
        out << alert(attrs, body)
    }

    def successContainer = { attrs, body ->
        attrs.id = attrs.id ?: 'success'
        attrs.alertClass = attrs.alertClass ?: 'alert-success'
        out << alert(attrs, body)
    }

    def warningContainer = { attrs, body ->
        attrs.id = attrs.id ?: 'warning'
        attrs.alertClass = attrs.alertClass ?: 'alert-warning'
        out << alert(attrs, body)
    }

    def messageContainer = { attrs, body ->
        attrs.id = attrs.id ?: 'message'
        attrs.alertClass = attrs.alertClass ?: 'alert-info'
        out << alert(attrs, body)
    }

    def alert = { attrs, body ->
        def visibleClass = body().size() ? 'show' : 'hide'
        out << '<div id="' + attrs.id + '" class="alert ' + attrs.alertClass + ' ' + visibleClass + '">'
        out << body()
        out << '</div>'
    }

    /**
     * create a bootstrappy modal
     * @attr id String The id for the dialog
     * @attr title String The modal title
     * @attr closable Boolean Can the modal be closed? default=true
     * @attr body String the markup inside the modal
     * @attr footer String the footer contents
     */

    def modal = { attrs, body ->
        if (!attrs?.title) throw new Exception('Title is required.')
        def canClose = attrs.closable.toString() == 'true' ? true : false


        out << """
            <div class="modal" id="${attrs.id}" tabindex="-1" role="dialog">
              <div class="modal-dialog">
                <div class="modal-content">
                  <div class="modal-header">
                    ${
            canClose ? '<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>' : ''
        }
                    <h4 class="modal-title">${attrs.title}</h4>
                  </div>
                  <div class="modal-body">
                    ${body()}
                  </div>
                  ${attrs?.footer ? '<div class="modal-footer">' + attrs.footer + '</div>' : ''}
                </div>
              </div>
            </div>
        """
    }

    Closure navMenu = { attrs ->
        attrs.menu = attrs.menu ?: []
        attrs.header = attrs.header ?: [:]
        attrs.containerId = attrs.containerId ?: 'lower-nav'
        attrs.depth = 0
        attrs.navClass = attrs.navClass ?: 'navbar-default navbar-custom navbar-fixed-top navbar-admin'

        out << """
        <nav class="navbar ${attrs.navClass}">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#lower-nav">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
        """
        if (attrs.header?.title) {
            if (attrs.header?.controller && attrs.header?.action) {
                out << g.link(class: 'navbar-brand', controller: attrs.header.controller, action: attrs.header.action) {
                    attrs.header.title.encodeAsHTML()
                }
            } else if (attrs.header?.mapping) {
                out << g.link(class: 'navbar-brand', mapping: attrs.header.mapping) {
                    attrs.header.title.encodeAsHTML()
                }
            } else {
                out << attrs.header.title
            }
        }
        out << """
                </div>

                <div id="${attrs.containerId}" class="navbar-collapse collapse">
                    ${ui.portalMenuDynamicContent(attrs)}
                </div>
            </div>
        </nav>
        """
    }

    Closure portalMenuDynamicContent = { attrs ->
        attrs.menu = attrs.menu ?: []
        attrs.depth = attrs.depth ?: 0

        def ulClass = attrs.ulClass ?: 'nav navbar-nav'
        def liClass = attrs.liClass ?: 'dropdown'
        def subMenuClass = attrs.subMenuClass ?: 'dropdown-toggle dropdown-title'
        if (attrs.depth >= 1) {
            ulClass = 'dropdown-menu'
            liClass = ''
            subMenuClass = 'dropdown-submenu'
        }

        out << "<ul class='${ulClass}'>"
        attrs.menu.each { item ->
            if (item?.mapping || item?.controller || item?.submenu) {
                out << "<li class='${item?.submenu ? subMenuClass : liClass} ${item.active ? 'active' : ''}'>"
                if (item?.mapping) {
                    out << g.link(mapping: item.mapping, class: "nav-links") { item.text.encodeAsHTML() }
                } else if (item?.controller) {
                    if (item?.action) {
                        out << g.link(controller: item.controller, action: item.action, params: item.params ?: [:], class: "nav-links") {
                            item.text.encodeAsHTML()
                        }
                    } else {
                        out << g.link(controller: item.controller, class: 'nav-links') { item.text.encodeAsHTML() }
                    }
                } else if (item?.submenu) {
                    out << """
                    <a href="#" class="${subMenuClass}" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
                        ${item.text.encodeAsHTML()} ${attrs.depth == 0 ? '<span class="caret"></span>' : ''}
                    </a>
                """
                }

                if (item?.submenu) {
                    out << portalMenuDynamicContent([menu: item.submenu, depth: attrs.depth + 1])
                }
                out << '</li>'
            }
        }
        out << '</ul>'
    }

    def toggleLink = { attrs, body ->
        def enabled = attrs['enabled'].toString() == 'true' ? true : false

        String clazz = 'disabled-link ' + attrs.class
        if (clazz.contains(' btn ')) {
            clazz += ' btn-disabled'
        }
        if (!enabled) {
            out << '<span class="' + clazz + '">'
            out << body()
            out << '</span>'
        } else {
            out << g.link(attrs, body)
        }
    }

    def truncatePost = { attrs ->
        String result
        def content = attrs?.article
        content = content.replaceAll(/<!--.*?-->/, '').replaceAll(/<.*?>/, '').replaceAll('\\[(.*?)\\]', '')
        int contentLength = attrs?.contentLength ? (attrs?.contentLength as int) : 150
        //Is content > than the contentLength?
        if (content.size() > contentLength) {
            BreakIterator bi = BreakIterator.getWordInstance()
            bi.setText(content);
            def first_after = bi.following(contentLength)

            //Truncate
            result = content.substring(0, first_after) + "..."
        } else {
            result = content
        }
        out << (result.size() ? result : 'No preview available...')
    }

    def disqus = { attrs ->
        def id = attrs?.id
        def url = grailsApplication.config.grails.serverURL + createLink(action: actionName, controller: controllerName, params: [id: id])
        out << """
            <div id="disqus_thread"></div>
            <script>

            /**
            *  RECOMMENDED CONFIGURATION VARIABLES: EDIT AND UNCOMMENT THE SECTION BELOW TO INSERT DYNAMIC VALUES FROM YOUR PLATFORM OR CMS.
            *  LEARN WHY DEFINING THESE VARIABLES IS IMPORTANT: https://disqus.com/admin/universalcode/#configuration-variables*/
            var disqus_config = function () {
                this.page.url = '${url}';  // Replace PAGE_URL with your page's canonical URL variable
                this.page.identifier = '${id}'; // Replace PAGE_IDENTIFIER with your page's unique identifier variable
            };
            (function() { // DON'T EDIT BELOW THIS LINE
            var d = document, s = d.createElement('script');
            s.src = '//${Environment.current == Environment.DEVELOPMENT ? 'local-recursive-codes' : 'recursive-codes'}.disqus.com/embed.js';
            s.setAttribute('data-timestamp', +new Date());
            (d.head || d.body).appendChild(s);
            })();
            </script>
            <noscript>Please enable JavaScript to view the <a href="https://disqus.com/?ref_noscript">comments powered by Disqus.</a></noscript>
        """
    }

    def gist = { attrs ->
        out << """<script src="https://gist.github.com/${grailsApplication.config.codes.recursive.github.user}/${
            attrs?.id
        }.js"></script>"""
    }

    def youtube = { attrs ->
        out << """
            <iframe width="854" height="480" src="https://www.youtube.com/embed/${attrs?.id}" frameborder="0" allowfullscreen></iframe>
        """
    }

    def render = { attrs, body ->
        def post = attrs?.post
        post = post.replaceAll("\\[gist(.*?)\\]", { full, word -> gist(id: word.tokenize('=').last()) })
        post = post.replaceAll("\\[youtube(.*?)\\]", { full, word -> youtube(id: word.tokenize('=').last()) })
        out << post
    }

    def googleAnalytics = { attrs, body ->
        def js = """
          (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
          (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
          m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
          })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

          ga('create', 'UA-1124632-10', 'auto');
          ga('send', 'pageview');
        """
        if (Environment.current == Environment.PRODUCTION) {
            out << g.javascript(null, js)
        }
    }
}
