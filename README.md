# AllegroRepos
<p>Application uses github REST API which is limited to 60 requests per hour, that's why it's possible to use a personal github OAtuh token which should allow 5000 requests per hour</p>
<p>Installation possible on devices with sdk version 26+ (android version 8+), project should open in Android Studio 4.1.3 without problems</p>
<p>Starting screen allows to use the personal github token but the application doesn't check if it's correct (checking TODO)</p>
<p>Screen with details has only information form GET /repos/{owner}/{repo} and is limited only to the part of information that could be taken directly from the response, additionally clicking on number of stargazers/forks/issues allows to see usernames of users how left a star/forked or titles of issues, unfortunately backing form list makes another request to get details (stopping it is TODO), details could be expanded by number of contributors/branches/commits/languages/tags/releases and list of them, issues could also have the date of creation</p>
<p>Application could also be expnaded by adding text formatting, making it possible to see repos of any user</p>
<p>Application supports polish language</p>
<p>details of repo is a bit broad term</p>
