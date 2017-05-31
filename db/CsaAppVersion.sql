/*  App version manual
	APP_VERSION: The version of the app. The version is separated to three numbers. The first two are app versions that will increase when a new version
	             of the app is pushed to the store. The last part is the content version. It will increase when there is correction of content or message to broadcast.
	STATUS: The indicator for each combination of app version and sub version.
	TITLE: The title of the pop up message on start of the app.
	MESSAGE: The message of the pop up message on start of the app.
	UPDATES: Survival guide content update queries. Multiple queries are separated by semi colon.
	
	Status:
	OK: Client is up to date.
	AU: App Update required. This will happen when APP_VERSION get increased, namely, we push a new version to the store.
	BM: Broadcast Message. A message that is sent to all clients.
	IBM: Apple Broadcast Message. A message that is sent to all Apple clients.
	ABM: Android Broadcast Message. A message that is sent to all Android clients.
	CU: Content Update. Updates on contents of survival guide.
*/

INSERT INTO WC_APP_VERSIONS (APP_VERSION, STATUS, TITLE, MESSAGE, UPDATES)
VALUES ('1.00.001', 'OK', '', '', '');

