package com.mobiarmy.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlDumpLoader {

    public static void loadIntoSqlite(String dumpPath, Connection conn) throws IOException {
        String sql = new String(Files.readAllBytes(Paths.get(dumpPath)), "UTF-8");
        int ok = 0, skipped = 0;

        for (String raw : splitStatements(sql)) {
            String statement = raw.trim();
            if (statement.isEmpty()) continue;
            
            String upper = statement.toUpperCase();
            if (upper.startsWith("ALTER TABLE")) { skipped++; continue; }
            if (upper.startsWith("SET ")) { skipped++; continue; }
            if (upper.startsWith("START TRANSACTION")) { skipped++; continue; }
            if (upper.equals("COMMIT")) { skipped++; continue; }
            if (upper.equals("BEGIN")) { skipped++; continue; }
            if (upper.equals("ROLLBACK")) { skipped++; continue; }

            if (upper.startsWith("CREATE TABLE")) {
                statement = sanitizeCreateTable(statement);
            }

            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(statement);
                ok++;
            } catch (Exception e) {
                skipped++;
                String preview = statement.length() > 120 ? statement.substring(0, 120) + "..." : statement;
                System.out.println("[SqlDumpLoader] Skipped statement (" + e.getMessage() + "): " + preview);
            }
        }
        System.out.println("[SqlDumpLoader] Loaded " + dumpPath + ": " + ok + " statements applied, " + skipped + " skipped.");
    }

    private static String sanitizeCreateTable(String createStatement) {
        String s = createStatement;
        s = Pattern.compile("CHARACTER SET \\w+", Pattern.CASE_INSENSITIVE).matcher(s).replaceAll("");
        s = Pattern.compile("COLLATE=?\\s*\\w+", Pattern.CASE_INSENSITIVE).matcher(s).replaceAll("");
        s = Pattern.compile("\\)\\s*ENGINE\\s*=\\s*\\w+[^;]*$", Pattern.CASE_INSENSITIVE).matcher(s).replaceAll(")");
        return s;
    }

    private static List<String> splitStatements(String sql) {
        List<String> statements = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inString = false;
        char quoteChar = '\0';
        int i = 0;
        int n = sql.length();

        while (i < n) {
            char c = sql.charAt(i);

            if (inString) {
                current.append(c);
                if (c == '\\' && i + 1 < n) {
                    current.append(sql.charAt(++i));
                } else if (c == quoteChar) {
                    if (i + 1 < n && sql.charAt(i + 1) == quoteChar) {
                        current.append(sql.charAt(++i));
                    } else {
                        inString = false;
                    }
                }
                i++;
                continue;
            }

            if (c == '\'' || c == '"') {
                inString = true;
                quoteChar = c;
                current.append(c);
                i++;
                continue;
            }

            if ((c == '-' && i + 1 < n && sql.charAt(i + 1) == '-') || c == '#') {
                while (i < n && sql.charAt(i) != '\n') i++;
                continue;
            }

            if (c == '/' && i + 1 < n && sql.charAt(i + 1) == '*') {
                i += 2;
                while (i + 1 < n && !(sql.charAt(i) == '*' && sql.charAt(i + 1) == '/')) i++;
                i += 2;
                continue;
            }

            if (c == ';') {
                statements.add(current.toString());
                current.setLength(0);
                i++;
                continue;
            }

            current.append(c);
            i++;
        }

        if (current.toString().trim().length() > 0) {
            statements.add(current.toString());
        }

        return statements;
    }
}
