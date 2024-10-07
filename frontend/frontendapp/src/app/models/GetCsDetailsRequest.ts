export interface GetCsDetailsRequest {
    numberOfTransformers: number,
    numberOfExpansionTanks: number,
    numberOfRadiators: number,
    numberOfConnectionPoints: number,
    numberOfFirewalls: number,
    lastDayUploaded?: Date
}