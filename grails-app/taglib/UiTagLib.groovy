import codes.recursive.blog.Post
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
        def pClass = attrs.panelClass ?: 'bg-primary text-white'
        def cClass = attrs.cardClass ?: '';
        def bClass = attrs.bodyClass ?: '';
        def showRefreshBtn = attrs.showRefreshBtn.toString() == 'true'
        def pId = attrs.panelId ?: UUID.randomUUID().toString().replaceAll('-', '')
        out << """
            <div class="card ${cClass}" id="${pId}">
				<div class="card-header  ${pClass}">
					<h5 class="card-title">${attrs.title}</h5>
				</div>
				<div class="card-body ${bClass}">
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
        if (!max) max = (attrs.int('max') ?: 12)

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

        writer << '<div class="d-flex justify-content-center"><ul class="pagination">'

        // determine paging variables
        def steps = maxsteps > 0
        int currentstep = (offset / max) + 1
        int firststep = 1
        int laststep = Math.round(Math.ceil(total / max))

        // display previous link when not on firststep unless omitPrev is true
        if (currentstep > firststep && !attrs.boolean('omitPrev')) {
            linkTagAttrs.class = 'page-link'
            linkParams.offset = offset - max
            writer << '<li class="page-item">' + link(linkTagAttrs.clone()) {
                (attrs.prev ?: messageSource.getMessage('paginate.prev', null, messageSource.getMessage('default.paginate.prev', null, 'Prev', locale), locale))
            } + '</li>'
        }

        // display steps when steps are enabled and laststep is not firststep
        if (steps && laststep > firststep) {
            linkTagAttrs.class = 'page-link'

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
                writer << '<li class="page-item">' + link(linkTagAttrs.clone()) { firststep.toString() } + '</li>'
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
                    writer << "<li class=\"d-none d-md-block page-item active\"><a class=\"page-link\" href=\"#\">${g.formatNumber(number: i, type: 'number')}</a></li>"
                } else {
                    linkParams.offset = (i - 1) * max
                    writer << '<li class="d-none d-md-block page-item">' + link(linkTagAttrs.clone()) {
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
                writer << '<li class="d-none d-md-block page-item">' + link(linkTagAttrs.clone()) {
                    g.formatNumber(number: laststep, type: 'number')
                } + '</li>'
            }
        }

        // display next link when not on laststep unless omitNext is true
        if (currentstep < laststep && !attrs.boolean('omitNext')) {
            linkTagAttrs.class = 'page-link'
            linkParams.offset = offset + max
            writer << '<li class="page-item">' + link(linkTagAttrs.clone()) {
                (attrs.next ? attrs.next : messageSource.getMessage('paginate.next', null, messageSource.getMessage('default.paginate.next', null, 'Next', locale), locale))
            } + '</li>'
        }

        writer << '</ul></div>'
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
        def lg = attrs?.large.toString() == "true" ? 'modal-lg' : ''
        def canClose = attrs.closable.toString() == 'true' ? true : false


        out << """
            <div class="modal" id="${attrs.id}" tabindex="-1" role="dialog">
              <div class="modal-dialog ${lg}">
                <div class="modal-content">
                  <div class="modal-header">
                    <h4 class="modal-title">${attrs.title}</h4>
                    ${
            canClose ? '<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>' : ''
        }
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
        attrs.navClass = attrs.navClass ?: 'navbar-dark bg-dark navbar-expand-lg'

        out << """
        <nav class="navbar ${attrs.navClass}">
            <div class="container-fluid">
                <a href="/admin/index" class="d-none d-md-flex align-items-center navbar-brand outlined-text-lg">
                    <span class="fs-3 fw-bold">${attrs.header.title.encodeAsHTML()}</span>
                </a>
                <button class="ms-auto bg-transparent text-light border-light navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#basic-navbar-nav"
                        aria-controls="basic-navbar-nav" aria-expanded="false" aria-label="Toggle navigation">
                    <i class="fas fa-bars"></i>
                </button>
                <div class="navbar-collapse collapse" id="basic-navbar-nav">
                    <div class="ms-auto navbar-nav">
                        ${ui.portalMenuDynamicContent(attrs)}
                    </div>
                </div>
            </div>
        </nav>
        """
    }

    Closure portalMenuDynamicContent = { attrs ->
        attrs.menu = attrs.menu ?: []
        attrs.depth = attrs.depth ?: 0

        def ulClass = attrs.ulClass ?: 'navbar-nav me-auto my-2 my-lg-0 navbar-nav-scroll'
        def liClass = attrs.liClass ?: ''
        def subMenuClass = attrs.subMenuClass ?: 'nav-link dropdown-toggle'
        if (attrs.depth >= 1) {
            ulClass = 'dropdown-menu'
            liClass = 'nav-item'
            subMenuClass = 'dropdown-submenu'
        }

        out << "<ul class='${ulClass}'>"
        attrs.menu.each { item ->
            if (item?.mapping || item?.controller || item?.submenu) {
                out << "<li class='${item?.submenu ? subMenuClass : liClass} ${item.active ? 'active' : ''}'>"
                if (item?.mapping) {
                    out << g.link(mapping: item.mapping, class: "nav-link") { item.text.encodeAsHTML() }
                } else if (item?.controller) {
                    if (item?.action) {
                        out << g.link(controller: item.controller, action: item.action, params: item.params ?: [:], class: "nav-link") {
                            item.text.encodeAsHTML()
                        }
                    } else {
                        out << g.link(controller: item.controller, class: 'nav-link') { item.text.encodeAsHTML() }
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

    def makeDescription = { attrs ->
        def content = attrs?.article
        def truncated = truncatePost(article: content)
        truncated = truncated.replaceAll('[\\r\\n]', ' ')
        out << truncated
    }

    def truncatePost = { attrs ->
        String result
        def content = attrs?.article
        content = content.replaceAll(/<!--.*?-->/, '').replaceAll(/<.*?>/, '').replaceAll('\\[(.*?)\\]', '')
        def context = attrs?.context
        int contentLength = attrs?.contentLength ? (attrs?.contentLength as int) : 150
        int contextPad = (contentLength / 2) + 1
        def contextIdx = context ? content.indexOf(context) : 0
        int contextStart = context ? contextIdx - contextPad : 0
        int contextEnd = context ? contextIdx + contextPad : content.size()
        if(contextStart < 0) contextStart = 0
        if(contextEnd > content.size()) contextEnd = content.size()

        if( context && contextIdx > -1 ) {
            content = content.substring( contextStart, contextIdx ) + content.substring( contextIdx, contextEnd )
        }
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

    def commento = { attrs ->
        out << """
            <div id="commento"></div>
            <script src="https://cdn.commento.io/js/commento.js"></script>
        """
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

    def gist2 = { attrs ->
        out << """<script src="https://gist.github.com/${grailsApplication.config.codes.recursive.github.user2}/${
            attrs?.id
        }.js"></script>"""
    }

    def spoiler = { attrs ->
        out << """<div><a href="#" class="showSpoiler">${attrs?.label ?: 'Spoiler'}</a><div class="spoiler">${attrs?.content}</div></div>"""
    }

    def youtube = { attrs ->
        out << """
            <iframe width="854" height="480" src="https://www.youtube.com/embed/${attrs?.id}" frameborder="0" allowfullscreen></iframe>
        """
    }

    def render = { attrs, body ->
        String post = attrs?.post
        String bannerImg = attrs?.bannerImg
        def js = """
                \$().ready(function(){
                    \$('.spoiler').hide()
                    \$(document).on('click', '.showSpoiler', function(){
                        \$(this).closest('div').find('.spoiler').show()
                        \$(this).hide()
                        return false
                    })
                })
            """
        out << g.javascript(null, js)
        def padBody = post.substring(0, 1) != '<p'
        if( bannerImg ) {
            post = """<img class="img-responsive img-thumbnail" src="${bannerImg}">""" + ( padBody ? '<p></p>' : '') + post
        }
        post = post.replaceAll("\\[spoiler(.*?)\\](.*?)\\[/spoiler\\]", { full, label, content -> spoiler(label: label.tokenize('=').last(), content: content) })
        post = post.replaceAll("\\[gist2(.*?)\\]", { full, word -> gist2(id: word.tokenize('=').last()) })
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
          var dimensionValue = 'SOME_DIMENSION_VALUE';
          ga('set', 'dimension1', dimensionValue);
          ga('create', 'UA-1124632-10', 'auto');
        """
        if( attrs?.tags ) {
            js += """
            var dimensionValue = '${attrs?.tags}';
            ga('set', 'dimension1', dimensionValue);
            """
        }
        js += """
          ga('send', 'pageview');
        """
        if (Environment.current == Environment.PRODUCTION) {
            out << g.javascript(null, js)
        }
    }

    def adsense = { attrs, body ->
        out << """
            <script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
            <!-- recursive-codes-responsive-1 -->
            <ins class="adsbygoogle"
                 style="display:block"
                 data-ad-client="ca-pub-1584600607873672"
                 data-ad-slot="5366767286"
                 data-ad-format="auto"></ins>
            <script>
            (adsbygoogle = window.adsbygoogle || []).push({});
            </script>
        """
    }
    
}
