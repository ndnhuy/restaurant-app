package main

import (
	"fmt"
	"os"
	"time"
	"math/rand"

	vegeta "github.com/tsenart/vegeta/v12/lib"
)

const RPS = 1;
const WAIT_TIME_IN_MS = 0; 
const PROCESS_TIME_IN_MS = 2000;

func main() {
	rate := vegeta.Rate{Freq: RPS, Per: time.Second}
	duration := 10 * time.Second
	urlBuild := func(baseUrl string, status string, waitTimeMs uint64, processTimeMs uint64) string {
		return fmt.Sprintf("%s/%s?waitTimeInMs=%d&processTimeInMs=%d", baseUrl, status, waitTimeMs, processTimeMs)
	}

	buildTargetUrl := func(status string) string {
		return urlBuild("http://localhost:8989/orders/create", status, WAIT_TIME_IN_MS, PROCESS_TIME_IN_MS)
	}
	newRandomTargeter := func (tgts ...vegeta.Target) vegeta.Targeter {
		return func(tgt *vegeta.Target) error {
			if tgt == nil {
				return vegeta.ErrNilTarget
			}
			*tgt = tgts[rand.Intn(len(tgts))]
			return nil
		}
	}
	targeter := newRandomTargeter(vegeta.Target{
		Method: "POST",
		URL:    buildTargetUrl("INIT"),
	}, vegeta.Target{
		Method: "POST",
		URL:    buildTargetUrl("PAID"),
	})
	attacker := vegeta.NewAttacker()

	var metrics vegeta.Metrics
	for res := range attacker.Attack(targeter, rate, duration, "Big Bang!") {
		metrics.Add(res)
	}
	metrics.Close()

	fmt.Println(fmt.Sprintf("RPS=%d, WaitTime=%dms, ProcessTime=%dms", RPS, WAIT_TIME_IN_MS, PROCESS_TIME_IN_MS))
	fmt.Printf("99th percentile: %s\n", metrics.Latencies.P99)

	reporter := vegeta.NewTextReporter(&metrics)
	reporter(os.Stdout)
}
