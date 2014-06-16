package hudson.plugins.view.dashboard.test;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.TopLevelItem;
import hudson.plugins.view.dashboard.DashboardPortlet;

import java.util.Collection;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.plugins.view.dashboard.Messages;

/**
 * Portlet that presents a grid of test result data with summation
 */
public class DetailedTestStatisticsPortlet extends TestStatisticsPortlet {
	@DataBoundConstructor
	public DetailedTestStatisticsPortlet(String name, String successColor, String failureColor, String skippedColor, boolean useBackgroundColors) {
		super(name, successColor, failureColor, skippedColor, useBackgroundColors);
	}
        
        @Override
	public TestResultSummary getTestResultSummary(Collection<TopLevelItem> jobs) {
		return TestUtil.getDetailedTestResultSummary(jobs);
	}

	@Extension
	public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

		@Override
		public String getDisplayName() {
			return Messages.Dashboard_DetailedTestStatisticsGrid();
		}
	}
}
