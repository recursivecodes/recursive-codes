<%@ page import="codes.recursive.blog.Post" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <asset:link rel="icon" href="favicon.ico" type="image/x-ico"/>
</head>
<body>
    <h1>Contact</h1>
    <form action="//formspree.io/toddraymondsharp@gmail.com" method="POST" id="contactform" class="form-horizontal">
        <input type="hidden" name="_next" value="${returnUrl}">
        <input type="hidden" name="_subject" id="_subject" value="Blog Contact Form">
        <input type="hidden" name="_format" value="plain">

        <input type="text" name="_gotcha" style="display:none">

        <div class="fieldcontainer form-group required">
        	<label class="col-sm-2 control-label" for="contact_name"><span class="required-indicator">*</span>Name</label>
        	<div class="col-sm-6">
                <input type="text" name="name" id="contact_name" class="form-control" required="">
        	</div>
        </div>

        <div class="fieldcontainer form-group required">
        	<label class="col-sm-2 control-label" for="contact_name"><span class="required-indicator">*</span>Email Address</label>
        	<div class="col-sm-6">
                <input type="text" name="name" id="contact_name" class="form-control" required="">
        	</div>
        </div>

        <div class="fieldcontainer form-group required">
        	<label class="col-sm-2 control-label" for="contact_comments"><span class="required-indicator">*</span>Comments</label>
        	<div class="col-sm-6">
                <textarea name="comments" id="contact_comments" rows="15" required="" class="form-control"></textarea><br>
        	</div>
        </div>

        <div class="fieldcontainer form-group required">
        	<label class="col-sm-2 control-label" for="contactSubmit">&nbsp;</label>
        	<div class="col-sm-4">
                <input type="submit" value="Send" id="contactSubmit" class="btn btn-primary btn-sm" />
        	</div>
        </div>

    </form>
</body>
</html>
