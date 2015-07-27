# kontraktor-spa-example

Shows how to use html imports (+ http4k's runtime shim for older browsers) to create a knockout application.

important:

* init knockout inside a document.ready callback
* app.js needs a simple loop at beginning to copy important resource templates to the main document. This is required during
development only as in productionmode kontraktor's http4k will inline and minify all html imports anyway.

from app.js, import template tags from html imports (required during dev only)
```javascript
// Important: add templates from all imports recursively to main document
function addTemplates(doc) {
  var link = doc.querySelectorAll('link[rel=import]');
  for (var i = 0; i < link.length; ++i)
  {
    if ( link[i].import ) {
      addTemplates(link[i].import);
      var contentArr = link[i].import.querySelectorAll('template');
      for (var ii = 0; ii < contentArr.length; ++ii) {
        document.body.appendChild(document.importNode(contentArr[ii], true));
      }
    }
  }
}
addTemplates(document);
//
```

ko init:
```java
// important: in order to make import shim work, put your init into document ready, as
// index.html tagged markup will be loaded AFTER processing imports (so early init of KO)
$('document').ready(function(){
  var model = new ViewModel();
  ko.punches.enableAll();
  ko.applyBindings(model);

  $(window).on('hashchange', function () {
    console.log("change site:" + window.location.hash.substring(1));
    model.currentView(window.location.hash.substring(1)); // bind url hash to template name
  });
});
```
