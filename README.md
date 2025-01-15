# Bloomberg-data-feed-monitoring

Project Overview
The Bloomberg Data Feed Monitoring System is an automated solution designed to fetch real-time stock data from Bloomberg’s financial data API (blpapi), monitor for anomalies, and process the data into an easily accessible format (Excel). The system is built using Java for data fetching, Python for automation and Excel handling, and email notifications to alert the team whenever an anomaly (e.g., bid being greater than ask) is detected.

This system runs at regular intervals (every 5 seconds), ensuring that stock data is continuously monitored and exported to an Excel file for easy access and reporting. In case any discrepancies are identified in the data, such as abnormal bid-ask values, an email notification is sent to all team members to alert them of the issue.

Features
Real-time Data Fetching: Fetches stock data such as bid, ask, and last price from Bloomberg's financial data API.
Anomaly Detection: Monitors stock data for anomalies (e.g., if the bid price is higher than the ask price) and triggers email alerts if such an anomaly is detected.
Automated Data Export: The system parses the stock data and exports it to an Excel file for easy tracking and analysis.
Scheduled Monitoring: Data fetching and processing are automated to run every 5 seconds, ensuring continuous monitoring without manual intervention.
Email Alerts: Sends real-time email notifications to the team when anomalies are detected in the stock data.
Data Logging: Stock data and anomalies are logged to an Excel file for long-term tracking and reporting.


Technologies Used
Java: Used to fetch real-time stock data from Bloomberg’s blpapi API.
Python: Handles automation, data parsing, anomaly detection, Excel file writing, and email alerts.
Bloomberg API (blpapi): Used for accessing real-time stock market data.
openpyxl: Python library to work with Excel files and save the stock data.
smtplib: Python library to send email alerts in case of detected anomalies.
