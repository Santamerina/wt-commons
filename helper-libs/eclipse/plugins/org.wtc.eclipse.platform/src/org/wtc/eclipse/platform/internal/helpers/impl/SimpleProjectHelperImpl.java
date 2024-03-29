/**
 * WT Commons Project 2008-2009
 *
 * http://code.google.com/p/wt-commons/wiki/WTSamples
 */
package org.wtc.eclipse.platform.internal.helpers.impl;

import junit.framework.TestCase;

import org.eclipse.swt.widgets.Text;
import org.wtc.eclipse.platform.helpers.EclipseHelperFactory;
import org.wtc.eclipse.platform.helpers.IProjectHelperConstants;
import org.wtc.eclipse.platform.helpers.ISimpleProjectHelper;
import org.wtc.eclipse.platform.helpers.IWorkbenchHelper;
import org.wtc.eclipse.platform.helpers.adapters.ProjectHelperImplAdapter;
import org.wtc.eclipse.platform.util.ExceptionHandler;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WidgetSearchException;
import com.windowtester.runtime.locator.IWidgetLocator;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.FilteredTreeItemLocator;
import com.windowtester.runtime.swt.locator.LabeledLocator;

/**
 * Helper for creating and manipulating simple projects.
 */
public class SimpleProjectHelperImpl extends ProjectHelperImplAdapter
    implements ISimpleProjectHelper {
    /**
     * @see  org.wtc.eclipse.platform.helpers.ISimpleProjectHelper#createProject(com.windowtester.runtime.IUIContext,
     *       java.lang.String)
     */
    public void createProject(IUIContext ui,
                              String projectName) {
        TestCase.assertNotNull(ui);
        TestCase.assertNotNull(projectName);

        logEntry2(projectName);

        IWorkbenchHelper workbench = EclipseHelperFactory.getWorkbenchHelper();
        workbench.listenForDialogOpenPerspective(ui);

        try {
            selectFileMenuItem(ui, IProjectHelperConstants.MENU_NEWPROJECT);
            ui.wait(new ShellShowingCondition("New Project")); //$NON-NLS-1$
            ui.click(new FilteredTreeItemLocator("(Simple|General)/Project")); //$NON-NLS-1$
            clickNext(ui);
            IWidgetLocator textRef = new LabeledLocator(Text.class, "&Project name:"); //$NON-NLS-1$
            safeEnterText(ui, textRef, projectName);
            clickFinish(ui);

            //wait for the project creation dialog to be dismissed
            ui.wait(new ShellDisposedCondition("New Project")); //$NON-NLS-1$

            waitForProjectExists(ui, projectName, true);
            workbench.waitNoResourceChangeEvents(ui);
        } catch (WidgetSearchException e) {
        	ExceptionHandler.handle(e);
        }

        workbench.waitNoJobs(ui);

        logExit2();
    }
}
