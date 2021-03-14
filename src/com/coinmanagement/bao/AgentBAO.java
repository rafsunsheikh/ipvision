package com.coinmanagement.bao;

import com.coinmanagement.dao.AgentDAO;
import com.coinmanagement.dto.agent.AgentAccountDTO;
import com.coinmanagement.dto.agent.AgentAuthenticationDTO;
import com.coinmanagement.dto.agent.AgentDTO;
import com.coinmanagement.dto.agent.AgentResponseDTO;
import com.coinmanagement.utils.HashGeneratorUtils;
//import com.coinmanagement.utils.Helper;
import com.coinmanagement.utils.Messages;
import com.coinmanagement.utils.ReasonCode;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author RingID Inc.
 */
public class AgentBAO {

    private static final Logger LOG = LogManager.getLogger(AgentBAO.class.getSimpleName());
    private static AgentBAO instance;

    private AgentBAO() {
    }

    public static AgentBAO getInstance() {
        if (instance == null) {
            instance = new AgentBAO();
        }

        return instance;
    }

    public AgentResponseDTO validateUserId(AgentAuthenticationDTO agent) {

        try {

            boolean verificationStatus = AgentDAO.getInstance().validateAgentLogUserId(agent.getEmail());
            if (verificationStatus) {
                return new AgentResponseDTO.Builder(false).mg(Messages.AGENT_DUPLICATE_ENTRY).
                        rc(ReasonCode.USER_EXISTS).build();
            } else {
                String verificationToken = "verified";
                boolean makeVerificationStatus = true;
                AgentDAO.getInstance().makeVerify(agent.getEmail(), verificationToken, makeVerificationStatus);
                AgentDTO agentDTO = AgentDAO.getInstance().validationProcess(agent.getEmail(), agent.getSHA256Password(agent.getPassword()));
                AgentDAO.getInstance().updateAgentAfterValidation(agentDTO);
                return new AgentResponseDTO.Builder(true).build();
            }
        } catch (Exception ex) {

            LOG.error("error in validateUserId --> " + ex);

            return new AgentResponseDTO.Builder(false).mg(Messages.AGENT_REGISTRATION_FAILURE).
                    rc(ReasonCode.EXCEPTION_OCCURED).build();

        }
    }

    public AgentResponseDTO resetAgentInfoFromUserIdAndPassword(AgentAuthenticationDTO authDTO) {

        try {
            if (authDTO.validLoginCredintial()) {
                String userId = authDTO.getEmail();
                String password = authDTO.getPassword();
                String newPassword = authDTO.getNewPassword();

                AgentDTO agentDTO = AgentDAO.getInstance().autheticateAgentWithUserIdAndPassword(userId, authDTO.getSHA256Password(password));

                if (agentDTO != null) {
                    LOG.info("agentDTO not null and active: " + agentDTO.getActive());
                   // agentDTO.setParentUserId(Helper.getUserIdFromId(agentDTO.getParentId()));
                    if (!agentDTO.getActive()) {
                        return new AgentResponseDTO.Builder(false).mg(Messages.AGENT_INACTIVE).rc(ReasonCode.ALREADY_NOT_ACTIVE).build();
                    }
                    AgentDAO.getInstance().resetPassword(userId, authDTO.getSHA256Password(newPassword));
                    List<AgentAccountDTO> agentWalletStatus = getAgentUserWalletStatus(agentDTO.getAgentId()).getAgentWalletStatus();
                    return new AgentResponseDTO.Builder(true).agentDetails(agentDTO).agentWalletStatus(agentWalletStatus)
                            .mg(Messages.AGENT_LOGIN_SUCCESS).rc(ReasonCode.NONE).build();
                } else {
                    agentDTO = AgentDAO.getInstance().authenticateAgentRegistrationLog(userId, authDTO.getSHA256Password(password));

                    if (agentDTO != null) {
                        if (agentDTO.getParentId() == -1 && agentDTO.getVerificationStatus()) {
                            return new AgentResponseDTO.Builder(false).mg(Messages.AGENT_ACCOUNT_PENDING).
                                    rc(ReasonCode.PENDING_RESELLER_REGISTRATION_REQUEST).build();
                        }

                        if (!agentDTO.getVerificationStatus()) {
                            return new AgentResponseDTO.Builder(false).mg(Messages.EMAIL_VERIFICATION_PENDING).rc(ReasonCode.EMAIL_IS_UNVERIFIED).build();
                        }
                    }
                }
            }

        } catch (Exception e) {
            LOG.error(e);
        }
        LOG.info("agentDTO null");
        return new AgentResponseDTO.Builder(false).mg(Messages.AGENT_INVALID_USERNAME_OR_PASSWORD).rc(ReasonCode.USER_DID_NOT_FOUND).build();
    }
    
    
    public AgentResponseDTO registerAgentInfo(AgentAuthenticationDTO authDTO) {
        
        try{
            if (authDTO.validLoginCredintial()){
                String userId = authDTO.getEmail();
                String password = authDTO.getPassword();
                int parentId = authDTO.getParentId();
                String name = authDTO.getName();
                String mobileNo = authDTO.getPhone();
                String address = authDTO.getAddress();
                String pin = authDTO.getPin();
                int type = authDTO.getType();
                String verificationToken = null;
                Long ringId = authDTO.getRingId();
                int coinCommission = authDTO.getCoinCommission();
                int goldCommission = authDTO.getGoldCommission();
                
                boolean registrationBool = AgentDAO.getInstance().insertAgentInfo(parentId, userId, name, authDTO.getSHA256Password(password), mobileNo, address, pin, type, verificationToken, ringId, coinCommission, goldCommission);
                if(registrationBool){
                    return new AgentResponseDTO.Builder(registrationBool).mg(Messages.AGENT_REGISTRATION_SUCCESS).rc(ReasonCode.SUCCESSFULL).build();
                }
                else{
                    return new AgentResponseDTO.Builder(registrationBool).mg(Messages.AGENT_REGISTRATION_FAILURE).rc(ReasonCode.NONE).build();
                }
            }
        }catch (Exception e) {
            LOG.error(e);
        }
        LOG.info("agentDTO null");
        return new AgentResponseDTO.Builder(false).mg(Messages.AGENT_INVALID_USERNAME_OR_PASSWORD).rc(ReasonCode.USER_DID_NOT_FOUND).build();
    }
    
    public AgentResponseDTO getStudentInfoFromStudentId(AgentAuthenticationDTO authDTO){
        try{
            AgentDAO.getInstance().checkStudentId(authDTO);
            return new AgentResponseDTO.Builder(true).mg(Messages.STUDENT_INSERT_OR_UPDATE_SUCCESSFUL).rc(ReasonCode.STUDENT_INSERT_SUCCESS).build();
        }catch(Exception e){
            
        }
        
        return new AgentResponseDTO.Builder(false).mg(Messages.STUDENT_INSERT_OR_UPDATE_UNSUCCESSFUL).rc(ReasonCode.STUDENT_INSERT_FAILURE).build();
    }
    public AgentResponseDTO getAgentInfoFromUserIdAndPassword(AgentAuthenticationDTO authDTO) {

        try {
            if (authDTO.validLoginCredintial()) {
                String userId = authDTO.getEmail();
                String password = authDTO.getPassword();

                AgentDTO agentDTO = AgentDAO.getInstance().autheticateAgentWithUserIdAndPassword(userId, authDTO.getSHA256Password(password));

                if (agentDTO != null) {
                    LOG.info("agentDTO not null and active: " + agentDTO.getActive());
                   // agentDTO.setParentUserId(Helper.getUserIdFromId(agentDTO.getParentId()));
                    if (!agentDTO.getActive()) {
                        return new AgentResponseDTO.Builder(false).mg(Messages.AGENT_INACTIVE).rc(ReasonCode.ALREADY_NOT_ACTIVE).build();
                    }
                    List<AgentAccountDTO> agentWalletStatus = getAgentUserWalletStatus(agentDTO.getAgentId()).getAgentWalletStatus();
                    return new AgentResponseDTO.Builder(true).agentDetails(agentDTO).agentWalletStatus(agentWalletStatus)
                            .mg(Messages.AGENT_LOGIN_SUCCESS).rc(ReasonCode.NONE).build();
                } else {
                    agentDTO = AgentDAO.getInstance().authenticateAgentRegistrationLog(userId, authDTO.getSHA256Password(password));

                    if (agentDTO != null) {
                        if (agentDTO.getParentId() == -1 && agentDTO.getVerificationStatus()) {
                            return new AgentResponseDTO.Builder(false).mg(Messages.AGENT_ACCOUNT_PENDING).
                                    rc(ReasonCode.PENDING_RESELLER_REGISTRATION_REQUEST).build();
                        }

                        if (!agentDTO.getVerificationStatus()) {
                            return new AgentResponseDTO.Builder(false).mg(Messages.EMAIL_VERIFICATION_PENDING).rc(ReasonCode.EMAIL_IS_UNVERIFIED).build();
                        }
                    }
                }
            }

        } catch (Exception e) {
            LOG.error(e);
        }
        LOG.info("agentDTO null");
        return new AgentResponseDTO.Builder(false).mg(Messages.AGENT_INVALID_USERNAME_OR_PASSWORD).rc(ReasonCode.USER_DID_NOT_FOUND).build();
    }

    public AgentResponseDTO getAgentUserWalletStatus(int agentId) {
        List<AgentAccountDTO> agentWalletStatus = new ArrayList<>();

        try {
            agentWalletStatus = AgentDAO.getInstance().getAgentWalletStatus(agentId);
            return new AgentResponseDTO.Builder(true).agentWalletStatus(agentWalletStatus).build();
        } catch (Exception ex) {
            LOG.error(ex);
        }

        return new AgentResponseDTO.Builder(false).mg(Messages.GENERIC_ERROR_MESSAGE).
                agentWalletStatus(agentWalletStatus).build();
    }
    
    

    private String getSHA256Password(String password) throws Exception {
        return HashGeneratorUtils.generateSHA256(password);
    }
}
