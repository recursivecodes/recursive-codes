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
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/js/all.min.js"></script>
    <link href='https://fonts.googleapis.com/css?family=Lora:400,700,400italic,700italic' rel='stylesheet' type='text/css'>
    <link href='https://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,300,600,700,800' rel='stylesheet' type='text/css'>
    <asset:javascript src="admin-application.js"/>
    <g:layoutHead/>
</head>

<body>

    <nav class="navbar p-0 navbar-default navbar-custom navbar-dark bg-dark navbar-expand-lg">
        <div class="container-fluid">
            <a href="/admin/index" class="d-none d-md-flex align-items-center navbar-brand outlined-text-lg p-0">
                <span class="fs-3 fw-bold">Admin</span>
            </a>
            <button class="ms-auto bg-transparent text-light border-light navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#basic-navbar-nav"
                    aria-controls="basic-navbar-nav" aria-expanded="false" aria-label="Toggle navigation">
                <i class="fas fa-bars"></i>
            </button>
            <div class="navbar-collapse collapse" id="basic-navbar-nav">
                <ul class="navbar-nav me-auto my-2 my-lg-0 navbar-nav-scroll" style="--bs-scroll-height: 100px;">
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page" href="/page/index">Blog</a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="userDd" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            Users
                        </a>
                        <ul class="dropdown-menu" aria-labelledby="userDd">
                            <li><a class="dropdown-item" href="/user/list">List Users</a></li>
                            <li><a class="dropdown-item" href="/user/edit">New User</a></li>
                        </ul>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="postsDd" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            Posts
                        </a>
                        <ul class="dropdown-menu" aria-labelledby="postsDd">
                            <li><a class="dropdown-item" href="/blog/list">List Posts</a></li>
                            <li><a class="dropdown-item" href="/blog/edit">New Post</a></li>
                        </ul>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="subsDd" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            Subscribers
                        </a>
                        <ul class="dropdown-menu" aria-labelledby="subsDd">
                            <li><a class="dropdown-item" href="/subscriber/list">List Subscribers</a></li>
                            <li><a class="dropdown-item" href="/subscriber/edit">New Subscriber</a></li>
                        </ul>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link disabled" href="/index/logout" tabindex="-1" aria-disabled="true">Logout</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Main Content -->
    <div class="container-admin">
        <g:layoutBody/>
    </div>

</body>
</html>
