package main

import (
	"fmt"
	"os"
	"time"
	"math/rand"

	vegeta "github.com/tsenart/vegeta/v12/lib"
)

func main() {
	rate := vegeta.Rate{Freq: 5, Per: time.Second}
	duration := 10 * time.Second
	statuses := []string{"INIT", "PAID"}
	status := statuses[rand.Intn(len(statuses))]
	targeter := vegeta.NewStaticTargeter(vegeta.Target{
		Method: "POST",
		URL: "http://localhost:8989/orders/create/" + status,
	})
	attacker := vegeta.NewAttacker()

	var metrics vegeta.Metrics
	for res := range attacker.Attack(targeter, rate, duration, "Big Bang!") {
		metrics.Add(res)
	}
	metrics.Close()

	fmt.Printf("99th percentile: %s\n", metrics.Latencies.P99)

	reporter := vegeta.NewTextReporter(&metrics)
	reporter(os.Stdout)
}
