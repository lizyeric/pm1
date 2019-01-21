package com.sg.pcrf;

/**
 * Created by Kejia.wu on 2017/9/8.
 * The exception handling system design, contains 6.1.1	有效性错误 and 6.1.2	系统错误
 */
public interface ErrorConstants {

    public interface ResultCode{

        public static final String SUCCESS = "0";

        public static final String MSG_LENTH_ERROR = "000001";
        public static final String PARSER_ERROR = "000002";
        public static final String ENCODING_ERROR = "000003";
        public static final String CONTENT_DIDNOT_PARSER = "000004";
        public static final String PARAMETERS_ERROR = "000005";
        public static final String DATABASE_EXCEPTION = "000006";
        public static final String INTERVAL_ERROR = "000007";
        public static final String OPERATION_FILE_DIDNOT_EXIST = "240000";
        public static final String OPERATION_FORMAT_ERROR = "240001";
        public static final String OPERATION_FILE_SIZE_ZERO = "240002";
        public static final String OPERATION_DEFINED_NAME_ERROR = "240003";
        public static final String OTHER_FILE_LEVEL_ERROR = "249999";

        /**  The exception handling system design */
        public static final String INTERNAL_ERROR = "5001";
        public static final String FLOW_CONTROL_ENABLED = "5002";

        public static final String FILE_NO_FOUND = "1005";
        public static final String NO_RIGHT_TO_EXEC_COMMAND = "1006";
        public static final String OPERATION_ALREADY_EXIST = "1011";
        public static final String OPERATION_NOT_DEFINED = "1012";
        public static final String ROLE_ALREADY_EXIST = "1013";
        public static final String ROLE_NOT_DEFINED = "1014";

        public static final String OPERATOR_NOT_LOGIN = "1016";
        public static final String INVILD_PASSWORD = "1017";
        public static final String PASSWORD_NO_MATCH = "1018";
        public static final String TIME_OUT = "1019";
        public static final String PASSWORD_EMPTY = "1027";
        public static final String NUMBER_THRESHOLD_EXCEEDED = "1033";
        public static final String DEFAULT_ADMIN_OPERATOR_NOT_BEEN_CHANGED = "1060";
        public static final String PASSWORD_DONOT_CONTAINS_COMPLEXITY_CHAR = "1077";

        public static final String PASSWORD_INCRRECT_COUNT_OVERLOAD_ACCOUNT_LOCKED = "1078";
        public static final String PASSWORD_EXPIRED = "1080";
        public static final String DATABASE_ACCESS_FAILED = "1181";
        /**   The exception handling system design */

    }

    public interface ErrorDesc{
        /**   The exception handling system design */
        public static final String INTERNAL_ERROR = "Internal error";
        public static final String FLOW_CONTROL_ENABLED = "Flow control enabled";

        public static final String FILE_NO_FOUND = "File not found";
        public static final String NO_RIGHT_TO_EXEC_COMMAND = "No rights to execute this command";
        public static final String OPERATION_ALREADY_EXIST = "Operator already exist";
        public static final String OPERATION_NOT_DEFINED = "Operator not defined";
        public static final String ROLE_ALREADY_EXIST = "Role already exist";
        public static final String ROLE_NOT_DEFINED = "Role not defined";

        public static final String OPERATOR_NOT_LOGIN = "Operator not logged in";
        public static final String INVILD_PASSWORD = "Invalid password";
        public static final String PASSWORD_NO_MATCH = "Password doesn't match";
        public static final String TIME_OUT = "Time out";
        public static final String PASSWORD_EMPTY = "Password is null";
        public static final String NUMBER_THRESHOLD_EXCEEDED = "Number threshold exceeded";
        public static final String DEFAULT_ADMIN_OPERATOR_NOT_BEEN_CHANGED = "Default admin operator can not be deleted/modified";
        public static final String PASSWORD_DONOT_CONTAINS_COMPLEXITY_CHAR = "The password does not meet complexity requirement";

        public static final String PASSWORD_INCRRECT_COUNT_OVERLOAD_ACCOUNT_LOCKED = "The number of login attempts with the incrrect password exceeds that of the allowed attempts, and the accout is locked";
        public static final String PASSWORD_EXPIRED = "The password expired";
        public static final String DATABASE_ACCESS_FAILED = "Database access failed";
        /**   The exception handling system design */
    }

}
