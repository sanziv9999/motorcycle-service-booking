function removePathVariable() {
            var url = window.location.href;
            var indexOfSlash = url.lastIndexOf('/');
            if (indexOfSlash !== -1) {
                var cleanUrl = url.substring(0, indexOfSlash);
                history.replaceState({}, document.title, cleanUrl);
            }
        }