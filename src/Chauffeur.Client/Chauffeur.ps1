<#
.SYNOPSIS
    The script is designed to act as a client proxy that will notify the
    machines (that are hosting the Jenkins Chauffuer Service) that a new
    build should be installed.
.DESCRIPTION
    <jobName> - The name of the job in Jenkins that will be installed.
    <machineName> - The machine names of the computers hosting the Jenkins Chauffuer Service. 
.NOTES
    File Name      : Chauffeur.ps1
    Author         : Kyle Baesler
    Prerequisite   : PowerShell V2    
.EXAMPLE
    Chauffeur.ps1 Chauffeur_Nightly MACHINE_1 MACHINE_2 LOCALHOST
#>

if ($args.Length -lt 2) 
{
    echo "Missing arguments: <jobName> <machineName> [<machineNames>]"     
}
else 
{
    # Enumerate through all of the machine names O(n).
    for($i=1; $i -lt $args.Length; $i++) 
    {        
        try
        {               
            # Create the URL to the hosted service.
            $address = "http://" + $args[$i] + ":8080/Chauffeur.Jenkins.Services/ChauffeurService/?wsdl"   
            
            # Create the client.
            $client=New-WebServiceProxy -Uri $address -UseDefaultCredential
            if ($client) 
            {
                # Install the last successful build for the job.
                $build = $client.InstallLastSuccessfulBuild($args[0])
            
                # The build number that will be installed.
                echo $args[$i] + " - Last successful build requested to be installed: " + $build.Number 
            }
        }
        catch [system.exception]
        {
            echo $_.Exception.Message    
        }
    }
}