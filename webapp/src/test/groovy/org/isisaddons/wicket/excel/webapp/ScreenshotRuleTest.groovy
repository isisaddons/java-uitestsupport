package org.isisaddons.wicket.excel.webapp
import geb.junit4.GebReportingTest
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

import java.nio.file.Files

class ScreenshotRuleTest extends GebReportingTest  {

    @Rule
    public IsisWebServerRule webServerRule = new IsisWebServerRule();
    @Rule
    public ScreenshotRule screenshotRule = new ScreenshotRule();
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void whenScreenshotMatches() throws Exception {

        // given
        // ... no actual screenshots are left over from previous run
        File actualScreenshotForLoginPage = screenshotFileFor("whenScreenshotMatches-loginPage.actual.png")
        deleteIfExists(actualScreenshotForLoginPage)

        // when
        to LoginPage
        screenshotRule.assertMatches(browser, "loginPage");

        // then
        // ... does NOT write out actual, since matches expected.
        assert !actualScreenshotForLoginPage.exists()

    }

    @Test
    public void whenScreenshotDoesNotMatch() throws Exception {

        expectedException.handleAssertionErrors();

        // given
        // ... have an expected screenshot
        File expectedScreenshot = screenshotFileFor("whenScreenshotDoesNotMatch-loginPage.expected.png")
        assert expectedScreenshot.exists()

        // ... that no actual screenshots are left over from previous run
        File actualScreenshot = screenshotFileFor("whenScreenshotDoesNotMatch-loginPage.actual.png")
        deleteIfExists(actualScreenshot)

        // then
        // ... the test should fail
        expectedException.expectMessage(Matchers.containsString("Screenshot differs: expected"))
        expectedException.expectMessage(Matchers.containsString("p4merge"))

        // when
        to LoginPage
        screenshotRule.assertMatches(browser, "loginPage");

        // then ... an actual screenshot is written out (referenced in the assertion)
        assert actualScreenshot.exists()
    }

    @Test
    public void whenExpectedScreenshotDoesNotExist() throws Exception {

        expectedException.handleAssertionErrors();

        // given
        // ... there is NO expected screenshot
        File expectedScreenshot = screenshotFileFor("whenExpectedScreenshotDoesNotExist-loginPage.expected.png")
        assert !expectedScreenshot.exists()
        // ... no actual screenshots left over from previous run
        File actualScreenshot = screenshotFileFor("whenExpectedScreenshotDoesNotExist-loginPage.actual.png")
        deleteIfExists(actualScreenshot)

        // then
        // ... the test should fail
        expectedException.expectMessage(Matchers.containsString("No expected screenshot; written out actual"))
        expectedException.expectMessage(Matchers.containsString("PaintDotNet"))

        // when
        to LoginPage
        screenshotRule.assertMatches(browser, "loginPage");

        // and then
        // ... an actual screenshot is written out (referenced in the assertion)
        assert actualScreenshot.exists()
    }

    private static File screenshotFileFor(String fileName) {
        final File projectDir = new File(".");
        final File resourcesDir = new File(projectDir, "src/test/resources");

        return new File(resourcesDir, "org/isisaddons/wicket/excel/webapp/ScreenshotRuleTest_" +
                        fileName).getCanonicalFile().getAbsoluteFile()
    }

    private static void deleteIfExists(File file) {
        if (file.exists()) {
            Files.deleteIfExists(file.toPath());
        }
        assert !file.exists()
    }

}
