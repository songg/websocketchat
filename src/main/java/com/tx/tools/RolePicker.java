package com.tx.tools;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;

import com.tx.model.constant.RoleEnum;

public class RolePicker {

	List<RoleEnum> roles = new ArrayList<>();

	public RolePicker() {
		roles.add(RoleEnum.GUARD);
		roles.add(RoleEnum.SEER);
		roles.add(RoleEnum.WOLF);
		roles.add(RoleEnum.WOLF);
		roles.add(RoleEnum.FARMER);
		roles.add(RoleEnum.FARMER);
	}

	public int pick() {
		int randomIndex = RandomUtils.nextInt(roles.size());
		RoleEnum role = roles.get(randomIndex);
		roles.remove(role);
		return role.getRole();
	}

	public static void main(String[] args) {
		RolePicker rolePicker = new RolePicker();
		for (int i = 0; i < 6; i++) {
			System.out.println(rolePicker.pick());
		}
	}

}
