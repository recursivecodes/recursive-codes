<%@ page import="codes.recursive.blog.Post" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <asset:link rel="icon" href="favicon.ico" type="image/x-ico"/>
</head>
<body>

    <div class="pad-bottom-10">
        <g:if test="${flash.message}">
            <ui:successContainer>${flash.message}</ui:successContainer>
        </g:if>

        <g:if test="${flash.error}">
            <ui:errorContainer>${flash.error}</ui:errorContainer>
        </g:if>
    </div>

    <h1>Subscribe</h1>

    <div class="row">
        <div class="col-sm-12">
            <div class="alert alert-info">
                <div>If you'd like to receive an email each time a new post goes live, enter your email below and then follow the link to verify your email address. You can unsubscribe at any time via the link in each email that you receive. <b><i>You can expect on average 3-4 emails per month.</i></b></div>
        </div>
    </div>

    <div class="row">
        <div class="col-sm-8">
            <g:form useToken="true" action="${actionName}" method="POST" class="form-horizontal" params="${defaultParams}">
                <bootform:horizontalField field="email" required="true" labelColumnClass="col-sm-6" controlColumnClass="col-sm-6" label="Email" bean="${subscriber}" description="">
                    <g:textField type="text" id="email" name="email" class="form-control" required="true" maxlength="100" value="${subscriber.email}"/>
                </bootform:horizontalField>
                <div class="row">
                    <div class="col-lg-6 col-lg-offset-6">
                        <input type="submit" id="submit" name="subscribe" class="btn btn-primary " value="Subscribe" />
                    </div>
                </div>
            </g:form>
        </div>
    </div>
</body>
</html>
