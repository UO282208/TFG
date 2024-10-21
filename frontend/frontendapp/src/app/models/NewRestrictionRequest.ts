export interface NewRestrictionRequest {
    transformers: number;    
    expansionTanks: number;
    radiators: number;    
    connectionPoints: number;    
    firewalls: number;    
    startDate: Date;
    endDate: Date;
    shouldAppear: boolean;
}