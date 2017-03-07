<!doctype html>
<html lang="en" class="no-js">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>
        <g:layoutTitle default="recursive.codes"/>
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>

    <asset:stylesheet src="application.css"/>
    <link href='https://fonts.googleapis.com/css?family=Lora:400,700,400italic,700italic' rel='stylesheet' type='text/css'>
    <link href='https://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,300,600,700,800' rel='stylesheet' type='text/css'>
    <link rel="alternate" type="application/rss+xml" title="recursive.codes RSS Feed" href="${grailsApplication.config.grails.serverURL}/blog/feed" />
    <ui:googleAnalytics />
    <g:layoutHead/>
</head>

<body>

    <!-- Navigation -->
    <nav class="navbar navbar-default navbar-custom navbar-fixed-top">
        <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header page-scroll">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span>
                    Menu <i class="fa fa-bars"></i>
                </button>
            <a href="/#" class="navbar-brand">recursive.codes</a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav navbar-right">
                    <li>
                        <a href="/#">Home</a>
                    </li>
                    <li>
                        <g:link action="about" controller="page">About</g:link>
                    </li>
                    <li>
                        <g:link controller="page" action="contact">Contact</g:link>
                    </li>
                    <sec:ifLoggedIn>
                        <li>
                            <g:link controller="admin" action="index">Admin</g:link>
                        </li>
                    </sec:ifLoggedIn>
                    <sec:ifLoggedIn>
                        <li>
                            <g:link controller="logout" action="index">Logout</g:link>
                        </li>
                    </sec:ifLoggedIn>
                    <sec:ifNotLoggedIn>
                        <li>
                            <g:link controller="login" action="index">Login</g:link>
                        </li>
                    </sec:ifNotLoggedIn>
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container -->
    </nav>

    <!-- Page Header -->
    <!-- Set your background image for this header on the line below. -->
    <header class="intro-header" style="background-image: url('${assetPath(src: 'home-bg.jpg')}')">
        <div class="container">
            <div class="row">
                <div class="col-lg-9 col-lg-offset-1 col-md-10 col-md-offset-1">
                    <div class="site-heading">
                        <h1 class="hidden-sm hidden-xs">recursive(codes){}</h1>
                        <h2 class="hidden-xs hidden-md hidden-lg">recursive(codes){}</h2>
                        <h3 class="hidden-sm hidden-md hidden-lg">recursive(codes){}</h2>
                        <hr class="small">
                        <span class="subheading">The Personal Blog of Todd Sharp</span>
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
                <div class="col-lg-8 col-lg-offset-2 col-md-10 col-md-offset-1">
                    <ul class="list-inline text-center">
                        <li>
                            <a href="https://twitter.com/recursivecodes" target="_blank">
                                <span class="fa-stack fa-lg">
                                    <i class="fa fa-circle fa-stack-2x"></i>
                                    <i class="fa fa-twitter fa-stack-1x fa-inverse"></i>
                                </span>
                            </a>
                        </li>
                        <li>
                            <a href="https://instagram.com/toddrsharp" target="_blank">
                                <span class="fa-stack fa-lg">
                                    <i class="fa fa-circle fa-stack-2x"></i>
                                    <i class="fa fa-instagram fa-stack-1x fa-inverse"></i>
                                </span>
                            </a>
                        </li>
                        <li>
                            <a href="https://www.linkedin.com/in/toddrsharp/" target="_blank">
                                <span class="fa-stack fa-lg">
                                    <i class="fa fa-circle fa-stack-2x"></i>
                                    <i class="fa fa-linkedin fa-stack-1x fa-inverse"></i>
                                </span>
                            </a>
                        </li>
                        <li>
                            <a href="https://github.com/cfsilence" target="_blank">
                                <span class="fa-stack fa-lg">
                                    <i class="fa fa-circle fa-stack-2x"></i>
                                    <i class="fa fa-github fa-stack-1x fa-inverse"></i>
                                </span>
                            </a>
                        </li>
                    </ul>

                    <p class="copyright text-muted">Copyright &copy; Todd Sharp ${Calendar.getInstance().get(Calendar.YEAR)}</p>
                </div>
            </div>
        </div>
    </footer>

    <asset:javascript src="application.js"/>

</body>
</html>
