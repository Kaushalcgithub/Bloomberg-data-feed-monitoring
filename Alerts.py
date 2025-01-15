pip install openpyxl
pip install smtplib

import openpyxl
from openpyxl import Workbook
import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
import time

# Function to write stock data to Excel
def write_to_excel(stock_data):
    wb = Workbook()
    sheet = wb.active
    sheet['A1'] = 'Stock Symbol'
    sheet['B1'] = 'Bid'
    sheet['C1'] = 'Ask'
    sheet['D1'] = 'Timestamp'

    # Write data to excel
    for i, data in enumerate(stock_data, start=2):
        sheet[f'A{i}'] = data['symbol']
        sheet[f'B{i}'] = data['bid']
        sheet[f'C{i}'] = data['ask']
        sheet[f'D{i}'] = data['timestamp']

    wb.save("BloombergStockData.xlsx")

# Function to send an email alert
def send_email_alert(message):
    sender_email = "youremail@example.com"
    receiver_email = "kaushal.chaudhai@cibc.com"
    password = "yourpassword"

    msg = MIMEMultipart()
    msg['From'] = sender_email
    msg['To'] = receiver_email
    msg['Subject'] = "Bloomberg exchange data alert"

    body = MIMEText(message, 'plain')
    msg.attach(body)

    try:
        server = smtplib.SMTP('smtp.example.com', 587)
        server.starttls()
        server.login(sender_email, password)
        text = msg.as_string()
        server.sendmail(sender_email, receiver_email, text)
        server.quit()
    except Exception as e:
        print(f"Error sending email: {e}")

# Function to check for data anomalies
def check_for_anomalies(stock_data):
    for data in stock_data:
        if float(data['bid']) > float(data['ask']):
            message = f"Anomaly Detected: {data['symbol']} - Bid is greater than Ask"
            send_email_alert(message)

# Main Function
def main():
    stock_data = []
    while True:
        # Fetch new stock data (this part would be the result from your Java service or API call)
        stock_data.append({
            'symbol': 'AAPL',
            'bid': '150.50',
            'ask': '149.80',
            'timestamp': time.time()
        })

        # Check for anomalies
        check_for_anomalies(stock_data)

        # Write data to Excel
        write_to_excel(stock_data)

        time.sleep(5)  # Sleep for 5 seconds before fetching new data

if __name__ == "__main__":
    main()
