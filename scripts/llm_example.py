#!/usr/bin/env python3
"""
Simple CLI example to call an LLM (OpenAI Chat Completions) using `requests`.

Usage:
  pip install requests
  export OPENAI_API_KEY="sk-..."
  python scripts/llm_example.py "Write a two-line haiku about tea"

The script reads the API key from `OPENAI_API_KEY`, sends a chat completion
request, and prints the assistant's reply. It does a few retries on 5xx errors.
"""

import os
import sys
import time
import json
from typing import Optional

import requests

OPENAI_ENDPOINT = "https://api.openai.com/v1/chat/completions"
DEFAULT_MODEL = "gpt-4o-mini"


def call_llm(prompt: str, model: str = DEFAULT_MODEL, max_retries: int = 3) -> Optional[str]:
    api_key = os.environ.get("OPENAI_API_KEY")
    if not api_key:
        raise RuntimeError("OPENAI_API_KEY environment variable is not set")

    headers = {
        "Authorization": f"Bearer {api_key}",
        "Content-Type": "application/json",
    }

    payload = {
        "model": model,
        "messages": [{"role": "user", "content": prompt}],
        "max_tokens": 300,
    }

    backoff = 1.0
    for attempt in range(1, max_retries + 1):
        try:
            resp = requests.post(OPENAI_ENDPOINT, headers=headers, json=payload, timeout=30)
        except requests.RequestException as exc:
            if attempt == max_retries:
                raise
            time.sleep(backoff)
            backoff *= 2
            continue

        if resp.status_code == 200:
            try:
                data = resp.json()
            except json.JSONDecodeError:
                raise RuntimeError("Invalid JSON response from LLM API")

            # OpenAI Chat Completions: choices[0].message.content
            choices = data.get("choices") or []
            if choices:
                message = choices[0].get("message") or {}
                content = message.get("content")
                if content:
                    return content

            # fallback: check common fields
            return data.get("output") or data.get("completion") or json.dumps(data)

        # Retry on server errors
        if 500 <= resp.status_code < 600 and attempt < max_retries:
            time.sleep(backoff)
            backoff *= 2
            continue

        # For client errors, raise with detail
        try:
            err = resp.json()
        except Exception:
            err = resp.text
        raise RuntimeError(f"LLM API request failed ({resp.status_code}): {err}")


if __name__ == "__main__":
    if len(sys.argv) >= 2:
        prompt_text = " ".join(sys.argv[1:])
    else:
        prompt_text = input("Enter prompt: ")

    try:
        out = call_llm(prompt_text)
    except Exception as e:
        print(f"Error calling LLM: {e}", file=sys.stderr)
        sys.exit(1)

    print(out or "(no content returned)")
