package hudson.plugins.view.dashboard.test;

import hudson.model.Action;
import hudson.model.Job;
import hudson.model.Run;
import hudson.model.TopLevelItem;
import hudson.tasks.test.AbstractTestResultAction;
import hudson.tasks.test.AggregatedTestResultPublisher.TestResultAction;
import hudson.tasks.test.TestResultProjectAction;
import hudson.maven.reporters.SurefireAggregatedReport;
import hudson.tasks.test.MetaTabulatedResult;

import java.util.Collection;

public class TestUtil {

   /**
    * Summarize the last test results from the passed set of jobs, 
    * including the packages of the job. If a job
    * doesn't include any tests, add a 0 summary.
    * 
    * @param jobs
    * @return
    */
   public static TestResultSummary getDetailedTestResultSummary(Collection<TopLevelItem> jobs) {    
       TestResultSummary summary = new TestResultSummary();
       
       for (TopLevelItem item : jobs) {
           if (item instanceof Job) {
               Job job = (Job) item;
               
               // create summary for the last run
               Run run = job.getLastBuild();
               if (run != null) {
                    TestResult testResult = TestUtil.getTestResult(job.getLastBuild());
                    summary.addTestResult(testResult);


                    AbstractTestResultAction tra = run.getAction(AbstractTestResultAction.class);

                    if (tra != null) {
                        if (tra.getResult() instanceof MetaTabulatedResult) {
                             MetaTabulatedResult result = (MetaTabulatedResult)tra.getResult();

                             // add test results for the packages
                             for (hudson.tasks.test.TestResult child : result.getChildren()) {
                                 PackageResult sub = new PackageResult(child, child.getTotalCount(), child.getFailCount(), child.getSkipCount());
                                 testResult.getPackageResults().add(sub);
                             }
                        }
                    }
               }
           }
       }
      
       return summary;
   }
   
      /**
    * Summarize the last test results from the passed set of jobs.  If a job
    * doesn't include any tests, add a 0 summary.
    * 
    * @param jobs
    * @return
    */
   public static TestResultSummary getTestResultSummary(Collection<TopLevelItem> jobs) {
      TestResultSummary summary = new TestResultSummary();

       for (TopLevelItem item : jobs) {
           if (item instanceof Job) {
                Job job = (Job) item;
                boolean addBlank = true;
                TestResultProjectAction testResults = job.getAction(TestResultProjectAction.class);

                if (testResults != null) {
                    AbstractTestResultAction tra = testResults.getLastTestResultAction();

                    if (tra != null) {
                       addBlank = false;
                       summary.addTestResult(new TestResult(job, tra.getTotalCount(), tra.getFailCount(), tra.getSkipCount()));
                    }
                } else {
                    SurefireAggregatedReport surefireTestResults = job.getAction(SurefireAggregatedReport.class);
                    if (surefireTestResults != null) {
                       addBlank = false;
                       summary.addTestResult(new TestResult(job, surefireTestResults.getTotalCount(), surefireTestResults.getFailCount(), surefireTestResults.getSkipCount()));
                    }
                }
                
                if (addBlank) {
                    Run run = job.getLastBuild();
                    if (run != null) {
                        TestResult testResult = getTestResult(run);
                        if (testResult != null) {
                            addBlank = false;
                            summary.addTestResult(testResult);
                        } 
                    }
                }
                
                if (addBlank) {
                    summary.addTestResult(new TestResult(job, 0, 0, 0));
                }
           }
      }

      return summary;
   }

   public static TestResult getTestResult(Run run) {
      AbstractTestResultAction tra = run.getAction(AbstractTestResultAction.class);
      if (tra != null) {
         return new TestResult(run.getParent(), tra.getTotalCount(), tra.getFailCount(), tra.getSkipCount());
      } 
      
      SurefireAggregatedReport surefireTestResults = run.getAction(SurefireAggregatedReport.class);
      if (surefireTestResults != null) {
         return new TestResult(run.getParent(), surefireTestResults.getTotalCount(), surefireTestResults.getFailCount(), surefireTestResults.getSkipCount());
      }
      
      return new TestResult(run.getParent(), 0, 0, 0);
   }
}
