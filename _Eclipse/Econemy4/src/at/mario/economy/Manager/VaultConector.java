package at.mario.economy.Manager;

import java.util.List;

import org.bukkit.OfflinePlayer;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

public class VaultConector implements Economy {

	@Override
	public EconomyResponse bankBalance(String arg0) {
		return null;
	}

	@Override
	public EconomyResponse bankDeposit(String arg0, double arg1) {
		return null;
	}

	@Override
	public EconomyResponse bankHas(String arg0, double arg1) {
		return null;
	}

	@Override
	public EconomyResponse bankWithdraw(String arg0, double arg1) {
		return null;
	}

	@Override
	public EconomyResponse createBank(String arg0, String arg1) {
		return null;
	}

	@Override
	public boolean createPlayerAccount(String name) {
		MoneyManager.getInstance().setBalance(name, 0);
		return true;
	}

	@Override
	public boolean createPlayerAccount(String name, String world) {
		return createPlayerAccount(name);
	}

	@Override
	public String currencyNamePlural() {
		return "Dollars";
	}

	@Override
	public String currencyNameSingular() {
		return "Dollar";
	}

	@Override
	public EconomyResponse deleteBank(String arg0) {
		return null;
	}

	@Override
	public EconomyResponse depositPlayer(String name, double amnt) {
		MoneyManager.getInstance().addBalance(name, amnt);
		return new EconomyResponse(amnt, MoneyManager.getInstance().getBalance(name), ResponseType.SUCCESS, "");
	}

	@Override
	public EconomyResponse depositPlayer(String name, String world, double amnt) {
		return depositPlayer(name, amnt);
	}

	@Override
	public String format(double amnt) {
		return "$" + String.valueOf(amnt);
	}

	@Override
	public int fractionalDigits() {
		return 2;
	}

	@Override
	public double getBalance(String name) {
		return MoneyManager.getInstance().getBalance(name);
	}

	@Override
	public double getBalance(String name, String world) {
		return getBalance(name);
	}

	@Override
	public List<String> getBanks() {
		return null;
	}

	@Override
	public String getName() {
		return "Economy29";
	}

	@Override
	public boolean has(String player, double amnt) {
		return MoneyManager.getInstance().getBalance(player) >= amnt;
	}

	@Override
	public boolean has(String player, String world, double amnt) {
		return has(player, amnt);
	}

	@Override
	public boolean hasAccount(String arg0) {
		return false;
	}

	@Override
	public boolean hasAccount(String arg0, String arg1) {
		return false;
	}

	@Override
	public boolean hasBankSupport() {
		return false;
	}

	@Override
	public EconomyResponse isBankMember(String arg0, String arg1) {
		return null;
	}

	@Override
	public EconomyResponse isBankOwner(String arg0, String arg1) {
		return null;
	}

	@Override
	public boolean isEnabled() {
		return MoneyManager.getInstance().getPlugin().isEnabled();
	}

	@Override
	public EconomyResponse withdrawPlayer(String player, double amnt) {
		return new EconomyResponse(amnt, MoneyManager.getInstance().getBalance(player) - amnt, MoneyManager.getInstance().removeBalance(player, amnt) ? ResponseType.SUCCESS : ResponseType.FAILURE, "Insufficient funds.");
	}

	@Override
	public EconomyResponse withdrawPlayer(String player, String world, double amnt) {
		return withdrawPlayer(player, amnt);
	}

	@Override
	public EconomyResponse createBank(String arg0, OfflinePlayer arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer arg0, double arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer arg0, String arg1, double arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getBalance(OfflinePlayer arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getBalance(OfflinePlayer arg0, String arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean has(OfflinePlayer arg0, double arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean has(OfflinePlayer arg0, String arg1, double arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasAccount(OfflinePlayer arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasAccount(OfflinePlayer arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EconomyResponse isBankMember(String arg0, OfflinePlayer arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse isBankOwner(String arg0, OfflinePlayer arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer arg0, double arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer arg0, String arg1, double arg2) {
		// TODO Auto-generated method stub
		return null;
	}
}
