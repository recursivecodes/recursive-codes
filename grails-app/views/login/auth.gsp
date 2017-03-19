<html>
<head>
    <meta name="layout" content="main"/>
</head>

<body>
    <div class="row">
        <div class="col-sm-4 col-sm-offset-4">
            <g:if test='${flash.message}'>
                <ui:errorContainer>
                    <div class="login_message">${flash.message}</div>
                </ui:errorContainer>
            </g:if>
            <form action="${postUrl}" method="POST" id="loginForm"  autocomplete="off" class="pad-top-20">
                <ui:panel title="Login">
                    <div class="row pad-bottom-10">
                        <div class="col-lg-12">
                            <input type="text" name="username" placeholder="Username" class="form-control" />
                        </div>
                    </div>
                    <div class="row pad-bottom-10">
                        <div class="col-lg-12">
                            <input type="password" name="password" placeholder="Password" class="form-control" />
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">
                            <input type="submit" id="submit" name="login" class="btn btn-primary btn-xs" value="Login" />
                        </div>
                    </div>
                </ui:panel>
            </form>
        </div>
    </div>
    <script type='text/javascript'>
        (function () {
            document.forms['loginForm'].elements['username'].focus();
        })();
    </script>
</body>
</html>
