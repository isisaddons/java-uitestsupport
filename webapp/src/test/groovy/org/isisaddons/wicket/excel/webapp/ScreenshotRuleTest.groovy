package org.isisaddons.wicket.excel.webapp
import geb.junit4.GebReportingTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

import java.nio.file.Files

import static org.hamcrest.CoreMatchers.containsString

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
        File actualScreenshotForLoginPage = screenshotFile("whenScreenshotMatches-loginPage.actual.png")
        deleteIfExists(actualScreenshotForLoginPage)
        File actualScreenshotForHomePage = screenshotFile("whenScreenshotMatches-loginPage.actual.png")
        deleteIfExists(actualScreenshotForHomePage)

        // when
        to LoginPage

        // then
        screenshotRule.assertMatches(browser, "loginPage");
        assert actualScreenshotForLoginPage.exists()

        // and when
        login "sven", "pass"

        // then
        screenshotRule.assertMatches(browser, "homePage");
        assert actualScreenshotForHomePage.exists()
    }

    @Test
    public void whenScreenshotDoesNotMatch() throws Exception {

        expectedException.handleAssertionErrors();

        // given
        File expectedScreenshot = screenshotFile("whenScreenshotDoesNotMatch-loginPage.expected.png")
        assert !expectedScreenshot.exists()

        File actualScreenshot = screenshotFile("whenScreenshotDoesNotMatch-loginPage.actual.png")
        deleteIfExists(actualScreenshot)

        // then
        expectedException.expectMessage(containsString("Screenshot differs: expected"))
        expectedException.expectMessage(containsString("p4merge"))

        // when
        to LoginPage
        screenshotRule.assertMatches(browser, "loginPage");

        // then
        screenshotRule.assertMatches(browser, "homePage");
        assert actualScreenshot.exists()
    }

    @Test
    public void whenScreenshotDoesNotExist() throws Exception {

        expectedException.handleAssertionErrors();

        // given
        File actualScreenshot = screenshotFile("whenScreenshotDoesNotExist-loginPage.actual.png")
        deleteIfExists(actualScreenshot)

        // then
        expectedException.expectMessage(containsString("No expected screenshot; written out actual"))
        expectedException.expectMessage(containsString("PaintDotNet"))

        // and when
        to LoginPage
        screenshotRule.assertMatches(browser, "loginPage");

        // then
        assert actualScreenshot.exists()
    }

    private static File screenshotFile(String fileName) {
        final File projectDir = new File(".");
        final File resourcesDir = new File(projectDir, "src/test/resources");

        final File actualScreenshotExpectedToBeWrittenOut =
                new File(resourcesDir, "org/isisaddons/wicket/excel/webapp/ScreenshotRuleTest_" +
                        fileName)
        actualScreenshotExpectedToBeWrittenOut
    }

    private static void deleteIfExists(File file) {
        if (file.exists()) {
            Files.deleteIfExists(file.toPath());
        }
        assert !file.exists()
    }

}
