# Simple Grails Blog

An ultra simple blog written with Grails 3.2.4.  You can create users and write blog posts and tag them.  There are plenty of blog engines available, but this one is mine.  It's not super fancy, but it works well and I can change anything pretty easily.  Uses WYSIHTML5 for post authoring.
  
**Grails Version: 3.2.4**

**DataBase Engine: MySQL**

There are a few "special" tags available via the admin that you can use when authoring posts:

**[gist id=]:** Insert a gist from github. Pass the ID from the URL. Shortcut: click the GitHub button in the toolbar.

**[youtube id=]:** Embed a YouTube video. Pass the ID from the URL. Shortcut: click the YouTube button in the toolbar.

**[spoiler label="Spoiler"]content[/spoiler]:** Hide content until the reader clicks on the link rendered in the blog post. Pass a 'label' or what will be used as the trigger text for the spoiler content. Shortcut: click the 'spoiler' button in the toolbar.

**Editing A Blog Post**

![Blog Post Editor](https://s3.amazonaws.com/img.recursive.codes/Screenshot_20170812_235723.png)

**Running Locally**

```bash
-Doracle.net.wallet_location=/wallet-recursive-codes
-Doracle.net.authentication_services="(TCPS)"
-Doracle.net.tns_admin=/wallet-demodb
-Djavax.net.ssl.trustStore=/wallet-recursive-codes/cwallet.sso
-Djavax.net.ssl.trustStoreType=SSO
-Djavax.net.ssl.keyStore=/wallet-recursive-codes/cwallet.sso
-Djavax.net.ssl.keyStoreType=SSO
-Doracle.net.ssl_server_dn_match=true
-Doracle.net.ssl_version="1.2"
-Ddatasource.username=[Username]
-Ddatasource.password=[Strong Password]
-Ddatasource.url=jdbc:oracle:thin:@recursivecodes_low?TNS_ADMIN=/wallet-recursive-codes
```