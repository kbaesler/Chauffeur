/*
    .SYNOPSIS
        The script is designed to act as a client proxy that will notify the
        machines (that are hosting the Jenkins Chauffeur Service) that a new
        build should be installed.
    .NOTES
        File Name      : Chauffeur.groovy
        Author         : Kyle Baesler
        Prerequisite   : Groovy 1.8.9
                       : Groovy Postbuild Plugin (https://wiki.jenkins-ci.org/display/JENKINS/Groovy+Postbuild+Plugin)
 */

// The name of the computers that host the Chauffeur service.
def MACHINE_NAMES = []

// The port that the WCF service is hosted on.
def PORT = 8080

try {

    def result = manager.getResult()
    manager.listener.logger.println('Chauffeur.groovy: ' + result)

    if (result != "SUCCESS") {
        return;
    }

    def jobName = manager.envVars['JOB_NAME']
    def buildNumber = manager.envVars['BUILD_NUMBER']
    def machineNames = new ArrayList<String>()

    MACHINE_NAMES.eachWithIndex { String s, int i ->
        try {

            def url = new URL('http://' + s + ':' + PORT + '/Chauffeur.Jenkins.Services/ChauffeurService/rest/Install/' + jobName + '/' + buildNumber)
            manager.listener.logger.println('Chauffeur.groovy: ' + url)

            def text = url.getText()
            manager.listener.logger.println('Chauffeur.groovy: ' + text)
            machineNames.add(s)

        } catch (Exception e) {
            manager.listener.logger.println('Chauffeur.groovy: ' + e.message)
        }
    }

    manager.addInfoBadge(machineNames)

} catch (Exception e) {
    manager.listener.logger.println('Chauffeur.groovy: ' + e.printStackTrace())
}