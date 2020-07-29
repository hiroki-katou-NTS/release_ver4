package nts.uk.ctx.at.schedule.dom.shift.management;

import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Injectable;
import mockit.integration.junit4.JMockit;
import nts.arc.task.tran.AtomTask;
import nts.arc.testing.assertion.NtsAssert;
import nts.uk.ctx.at.schedule.dom.shift.management.CopyShiftPaletteByOrgService.Require;

@RunWith(JMockit.class)
public class CopyShiftPaletteByOrgServiceTest {

	@Injectable
	private Require require;

	/**
	 * require.組織別シフトパレットを存在するか(複製元のシフトパレット.対象組織, 複製先のページ) == true 上書きするか = false
	 */
	@Test
	public void testDuplicate_throw_1712() {
		ShiftPalletsOrg shiftPalletsOrg = ShiftPalletsOrgHelper.getShiftPalletsOrgDefault();
		int page = 1;
		ShiftPalletName shiftPalletName = new ShiftPalletName("shiftPalletName");
		boolean overwrite = false;

		new Expectations() {
			{
				require.exists(shiftPalletsOrg.getTargeOrg(), page);
				result = true;
			}
		};
		NtsAssert.businessException("Msg_1712", () -> {
			AtomTask persist = CopyShiftPaletteByOrgService.duplicate(require, shiftPalletsOrg, page, shiftPalletName,
					overwrite);
			persist.run();
		});
	}

	/**
	 * require.組織別シフトパレットを存在するか(複製元のシフトパレット.対象組織, 複製先のページ) == true 上書きするか = true;
	 */
	@Test
	public void testDuplicate() {
		ShiftPalletsOrg shiftPalletsOrg = ShiftPalletsOrgHelper.getShiftPalletsOrgDefault();
		int page = 1;
		ShiftPalletName shiftPalletName = new ShiftPalletName("shiftPalletName");
		boolean overwrite = true;

		new Expectations() {
			{
				require.exists(shiftPalletsOrg.getTargeOrg(), page);
				result = true;
			}
		};
		NtsAssert.atomTask(
				() -> CopyShiftPaletteByOrgService.duplicate(require, shiftPalletsOrg, page, shiftPalletName,
						overwrite),
				any -> require.delete(shiftPalletsOrg.getTargeOrg(), page), any -> require.add(any.get()));
	}

	/**
	 * require.組織別シフトパレットを存在するか(複製元のシフトパレット.対象組織, 複製先のページ) == false 上書きするか = true;
	 */
	@Test
	public void testDuplicate_2() {
		ShiftPalletsOrg shiftPalletsOrg = ShiftPalletsOrgHelper.getShiftPalletsOrgDefault();
		int page = 1;
		ShiftPalletName shiftPalletName = new ShiftPalletName("shiftPalletName");
		boolean overwrite = true;

		new Expectations() {
			{
				require.exists(shiftPalletsOrg.getTargeOrg(), page);
				result = true;
			}
		};
		NtsAssert.atomTask(() -> CopyShiftPaletteByOrgService.duplicate(require, shiftPalletsOrg, page, shiftPalletName,
				overwrite), any -> require.add(any.get()));
	}

}
