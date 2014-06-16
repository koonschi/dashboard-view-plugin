/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hudson.plugins.view.dashboard.test;

/**
 *
 * @author z002uz6a
 */
public class PackageResult {
    
   private hudson.tasks.test.TestResult packageResult;
   protected int tests;
   protected int success;
   protected int failed;
   protected int skipped;
   
   public PackageResult(hudson.tasks.test.TestResult packageResult, int tests, int failed, int skipped) {
      super();
      this.packageResult = packageResult;
      this.tests = tests;
      this.failed = failed;
      this.skipped = skipped;

      this.success = tests - failed - skipped;
   }

   public String getName() {
      return packageResult.getDisplayName();
   }

   public String getUrl() {
      return packageResult.getUrl();
   }

   public int getTests() {
      return tests;
   }

   public int getSuccess() {
      return success;
   }

   public double getSuccessPct() {
      return tests != 0 ? ((double) success / tests) : 0d;
   }

   public int getFailed() {
      return failed;
   }

   public double getFailedPct() {
      return tests != 0 ? ((double) failed / tests) : 0d;
   }

   public int getSkipped() {
      return skipped;
   }

   public double getSkippedPct() {
      return tests != 0 ? ((double) skipped / tests) : 0d;
   }
}
