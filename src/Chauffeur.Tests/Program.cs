﻿using System;
using System.Configuration;
using System.IO;

using Chauffeur.Windows.Services;

namespace Chauffeur.Tests
{
    internal class Program
    {
        #region Private Methods

        private static void Main(string[] args)
        {
            string jobName = ConfigurationManager.AppSettings["jenkins.job"];
            string directory = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.MyDocuments), "Jenkins");

            ChauffeurService chauffeurService = new ChauffeurService();
            chauffeurService.InstallLastSuccessfulBuild(jobName, directory);
        }

        #endregion
    }
}