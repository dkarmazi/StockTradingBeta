/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package StockTradingServer;

import StockTradingCommon.Enumeration;
/**
 *
 * @author Hirosh Wickramasuriya <hirosh@gwmail.gwu.edu>
 */
public class UserAdmin extends User{
    
    @Override
    public Validator validate() {

		InputValidation iv = new InputValidation();
		Validator vResult = new Validator();
		Validator vFirstName, vLastName, vEmail, vSsn, vPassword, vStatusId, vFirmId;

		Boolean verified = true;
		String status = "";

		// 1. validate first name
		vFirstName = iv.validateString(this.getFirstName(), "First Name");
		verified &= vFirstName.isVerified();
		status += vFirstName.getStatus();

		// 2. validate last name
		vLastName = iv.validateString(this.getLastName(), "First Name");
		verified &= vLastName.isVerified();
		status += vLastName.getStatus();

		// 3. validate email
		vEmail = iv.validateEmail(this.getEmail(), "Email");
		verified &= vEmail.isVerified();
		status += vEmail.getStatus();

		// 4. validate ssn
		vSsn = iv.validateString(this.getSsn(), "Ssn");
		verified &= vSsn.isVerified();
		status += vSsn.getStatus();

		// 5. validate password
		// vPassword = iv.validateString(this.getPassword(), "Password");
		// verified &= vPassword.isVerified();
		// status += vPassword.getStatus();

		// 6. status id
		vStatusId = iv.validateIntGeneral(this.getStatusId(), "StatusId");
		verified &= vStatusId.isVerified();
		status += vStatusId.getStatus();

		// 7. firm id
        vFirmId = iv.validateIntDefault(this.getBrokerFirmId(), Enumeration.BrokerageFirm.BROKERAGE_FIRM_ID_FOR_ADMIN, "FirmId");
		verified &= vFirmId.isVerified();
		status += vFirmId.getStatus();

		vResult.setVerified(verified);
		vResult.setStatus(status);

		return vResult;
	}
    
}
