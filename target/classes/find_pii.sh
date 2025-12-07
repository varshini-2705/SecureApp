#!/bin/bash
# PII Detection Script: find_pii.sh

# This script takes two arguments:
# ARG 1 ($1): The input data file path (e.g., /home/user/data_file.txt)
# ARG 2 ($2): The output file path for results (e.g., /home/user/pii_results/found_pii_data_file.txt)

INPUT_FILE="$1"
OUTPUT_FILE="$2"

echo "--- SCAN STARTED: $(date) for file: ${INPUT_FILE} ---" > "${OUTPUT_FILE}"

# Check if the input file exists
if [ ! -f "$INPUT_FILE" ]; then
    echo "ERROR: Input file not found at ${INPUT_FILE}" >> "${OUTPUT_FILE}"
    echo "--- SCAN ENDED: $(date) ---" >> "${OUTPUT_FILE}"
    exit 1
fi


# 1. Email detection (simple pattern)
echo "" >> "${OUTPUT_FILE}"
echo "--- Emails Found ---" >> "${OUTPUT_FILE}"
grep -E -o "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}" "$INPUT_FILE" >> "${OUTPUT_FILE}" || echo "None found" >> "${OUTPUT_FILE}"

#phone number detection 
echo "" >> "${OUTPUT_FILE}"
echo "--- Phone Numbers Found ---" >> "${OUTPUT_FILE}"
grep -P -o '\b[6-9]\d{9}\b' "$INPUT_FILE" >> "${OUTPUT_FILE}" || echo "None found" >> "${OUTPUT_FILE}"

# 3. Credit Card detection (16 digits with or without spaces/hyphens)
echo "" >> "${OUTPUT_FILE}"
echo "--- Credit Card Numbers Found (Basic 16-digit) ---" >> "${OUTPUT_FILE}"
grep -E -o "\b([0-9]{4}[- ]?[0-9]{4}[- ]?[0-9]{4}[- ]?[0-9]{4})\b" "$INPUT_FILE" >> "${OUTPUT_FILE}" || echo "None found" >> "${OUTPUT_FILE}"


# Check if any PII was actually found (excluding headers)
#if [ $(grep -v "---" "${OUTPUT_FILE}" | grep -v "ERROR" | wc -l) -le 3 ]; then
   # echo "No PII data found based on defined patterns." >> "${OUTPUT_FILE}"
#fi

echo "" >> "${OUTPUT_FILE}"
echo "--- SCAN ENDED: $(date) ---" >> "${OUTPUT_FILE}"

