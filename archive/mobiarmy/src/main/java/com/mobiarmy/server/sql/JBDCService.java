package com.mobiarmy.server.sql;

import java.sql.Connection;
import java.sql.Statement;

public interface JBDCService {
    public Connection getConnection();
    public Statement getStatement();
}
