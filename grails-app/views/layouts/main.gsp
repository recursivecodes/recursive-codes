<%@ page import="grails.util.Holders" %>
<!doctype html>
<html lang="en" class="no-js">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>
        <g:layoutTitle default="recursive.codes"/>
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>

    <asset:link rel="shortcut icon" href="favicon.ico" type="image/x-icon"/>
    <asset:stylesheet src="application.css"/>
    <script src="https://code.jquery.com/jquery-3.6.1.min.js" integrity="sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js" integrity="sha384-IQsoLXl5PILFhosVNubq5LC7Qb9DXgDA9i+tQ8Zj3iwWAwPtgFTxbJ8NT4GN1R8p" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js" integrity="sha384-cVKIPhGWiC2Al4u+LWgxfKTRIcfu0JTxR+EQDz/bgldoEyl4H0zUF0QKbrJ0EcQF" crossorigin="anonymous"></script>
    <asset:javascript src="application.js"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/js/all.min.js"></script>
    <link href='https://fonts.googleapis.com/css?family=Lora:400,700,400italic,700italic' rel='stylesheet' type='text/css'>
    <link href='https://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,300,600,700,800' rel='stylesheet' type='text/css'>
    <link rel="alternate" type="application/rss+xml" title="recursive.codes RSS Feed" href="${grailsApplication.config.grails.serverURL}/blog/feed" />
    <ui:googleAnalytics tags="${post?.postTags?.collect{it.tag.name}?.join(',')}" />
    <g:layoutHead/>
</head>

<body>

    <!-- Navigation -->
    <nav class="navbar p-0 navbar-default navbar-custom navbar-fixed-top navbar-expand-lg">
        <div class="container-fluid">
            <a href="/#" class="d-flex align-items-center navbar-brand outlined-text-lg">
                <span class="fs-3 fw-bold">recursive.codes</span>
            </a>
            <button class="ms-auto bg-transparent text-light border-light navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#basic-navbar-nav"
                    aria-controls="basic-navbar-nav" aria-expanded="false" aria-label="Toggle navigation">
                <i class="fas fa-bars"></i>
            </button>
            <div class="navbar-collapse collapse" id="basic-navbar-nav">
                <div class="ms-auto navbar-nav">
                    <g:link action="about" controller="page" class="nav-link text-white outlined-text">About</g:link>
                    <g:link action="videos" controller="page" class="nav-link text-white outlined-text">Videos</g:link>
                    <g:link action="presentations" controller="page" class="nav-link text-white outlined-text">Presentations</g:link>
                    <g:link controller="page" action="contact" class="nav-link text-white outlined-text">Contact</g:link>
                    <g:link controller="page" action="subscribe" class="nav-link text-white outlined-text">Subscribe</g:link>
                    <g:link controller="page" action="search" class="nav-link text-white outlined-text">Search</g:link>
                    <sec:ifLoggedIn>
                        <g:link controller="admin" action="index" class="nav-link text-white outlined-text">Admin</g:link>
                    </sec:ifLoggedIn>
                </div>
            </div>
        </div>
    </nav>

    <!-- Page Header -->
    <!-- Set your background image for this header on the line below. -->
    <header class="intro-header" style="background-image: url('${assetPath(src: 'banner.jpg')}')">
        <div class="container">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="site-heading">
                        <h1 class="d-none d-xl-block outlined-text">recursive.codes</h1>
                        <h2 class="d-none d-md-block d-xl-none outlined-text">recursive.codes</h2>
                        <h3 class="d-xs-block d-md-none outlined-text">recursive.codes</h3>
                        <hr class="small outlined-text">
                        <span class="subheading outlined-text">The Personal Blog of Todd Sharp</span>
                    </div>
                </div>
            </div>
        </div>
    </header>

    <!-- Main Content -->
    <div class="container">
        <g:layoutBody/>
    </div>

    <hr>

    <!-- Footer -->
    <footer>
        <div class="container">
            <div class="row">
                <div class="col">
                    <div class="text-center">
                        <div class="d-flex justify-content-center">
                            <a href="https://twitch.tv/recursivecodes" target="_blank">
                                <span class="fa-stack">
                                    <i class="fas fa-circle fa-stack-2x"></i>
                                    <i class="fab fa-twitch fa-stack-1x fa-inverse"></i>
                                </span>
                            </a>
                            <a href="https://twitter.com/recursivecodes" target="_blank">
                                <span class="fa-stack">
                                    <i class="fas fa-circle fa-stack-2x"></i>
                                    <i class="fab fa-twitter fa-stack-1x fa-inverse"></i>
                                </span>
                            </a>
                            <a href="https://instagram.com/toddrsharp" target="_blank">
                                <span class="fa-stack">
                                    <i class="fas fa-circle fa-stack-2x"></i>
                                    <i class="fab fa-instagram fa-stack-1x fa-inverse"></i>
                                </span>
                            </a>
                            <a href="https://www.linkedin.com/in/toddrsharp/" target="_blank">
                                <span class="fa-stack">
                                    <i class="fas fa-circle fa-stack-2x"></i>
                                    <i class="fab fa-linkedin fa-stack-1x fa-inverse"></i>
                                </span>
                            </a>
                            <a href="https://github.com/cfsilence" target="_blank">
                                <span class="fa-stack">
                                    <i class="fas fa-circle fa-stack-2x"></i>
                                    <i class="fab fa-github fa-stack-1x fa-inverse"></i>
                                </span>
                            </a>
                            <a href="https://github.com/recursivecodes" target="_blank">
                                    <span class="fa-stack">
                                        <i class="fas fa-circle fa-stack-2x"></i>
                                        <i class="fab fa-github fa-stack-1x fa-inverse"></i>
                                    </span>
                                </a>
                        </div>
                        <div class="mt-3 copyright text-muted">Copyright &copy; Todd Sharp ${Calendar.getInstance().get(Calendar.YEAR)}</div>
                        <div class="text-center" style="font-size: 14px;">
                            <div>
                                <small><g:link controller="page" action="privacy">Privacy Policy</g:link></small>
                            </div>
                            <sec:ifNotLoggedIn>
                                <div>
                                    <small><g:link controller="login" action="index">Login</g:link></small>
                                </div>
                            </sec:ifNotLoggedIn>
                            <sec:ifLoggedIn>
                                <div>
                                    <small><g:link controller="logout" action="index">Logout</g:link></small>
                                </div>
                            </sec:ifLoggedIn>
                            <div>
                                <small><i>Env: ${grails.util.Environment.current}</i></small>
                            </div>
                            <div>
                                <small><i>Build Commit: ${grails.util.Holders.getGrailsApplication().config?.codes?.recursive?.commitHash ?: 'unknown'}</i></small>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </footer>

</body>
</html>
